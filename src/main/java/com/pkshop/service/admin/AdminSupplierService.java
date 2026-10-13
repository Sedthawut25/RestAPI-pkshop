package com.pkshop.service.admin;

import com.pkshop.common.exception.BadRequestException;
import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.domain.supplier.entity.SupplierProduct;
import com.pkshop.domain.supplier.repository.SupplierProfileRepository;
import com.pkshop.domain.supplier.repository.SupplierProductRepository;
import com.pkshop.domain.user.entity.Role;
import com.pkshop.domain.user.entity.User;
import com.pkshop.domain.user.repository.RoleRepository;
import com.pkshop.domain.user.repository.UserRepository;
import com.pkshop.dto.admin.supplier.CreateAdminSupplierRequest;
import com.pkshop.dto.admin.supplier.CreateSupplierProductRequest;
import com.pkshop.dto.admin.supplier.AdminSupplierListResponse;
import com.pkshop.dto.admin.supplier.SupplierProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AdminSupplierService {

    private final SupplierProfileRepository supplierProfileRepository;
    private final SupplierProductRepository supplierProductRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<AdminSupplierListResponse> listSuppliers(
            int page,
            int size
    ) {
        return supplierProfileRepository.findAdminSuppliers(
                PageRequest.of(page, size)
        );
    }

    @Transactional
    public AdminSupplierListResponse createSupplier(CreateAdminSupplierRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("อีเมลนี้มีบัญชีในระบบแล้ว");
        }

        Role supplierRole = roleRepository.findByName("SUPPLIER")
                .orElseThrow(() -> new BadRequestException("ไม่พบ role SUPPLIER ในระบบ"));

        User user = new User();
        user.setEmail(email);
        user.setFullName(request.fullName().trim());
        user.setPhone(blankToNull(request.phone()));
        user.setPasswordHash(passwordEncoder.encode(request.initialPassword()));
        user.setAuthProvider("LOCAL");
        user.setStatus("ACTIVE");
        user.getRoles().add(supplierRole);
        User savedUser = userRepository.save(user);

        var profile = new com.pkshop.domain.supplier.entity.SupplierProfile();
        profile.setUserId(savedUser.getId());
        profile.setCompanyName(request.companyName().trim());
        profile.setContactName(blankToNull(request.contactName()) != null
                ? request.contactName().trim()
                : savedUser.getFullName());
        profile.setContactEmail(blankToNull(request.contactEmail()) != null
                ? request.contactEmail().trim().toLowerCase(Locale.ROOT)
                : savedUser.getEmail());
        profile.setContactPhone(blankToNull(request.contactPhone()) != null
                ? request.contactPhone().trim()
                : savedUser.getPhone());
        profile.setCreatedAt(LocalDateTime.now());
        supplierProfileRepository.save(profile);

        return new AdminSupplierListResponse(
                savedUser.getId(),
                profile.getCompanyName(),
                profile.getContactName(),
                profile.getContactEmail(),
                profile.getCountry(),
                profile.getContactPhone(),
                profile.getCreatedAt()
        );
    }

    @Transactional
    public AdminSupplierListResponse updateSupplier(Long supplierUserId, com.pkshop.dto.admin.supplier.UpdateAdminSupplierRequest request) {
        User supplier = requireSupplier(supplierUserId);
        if ("INACTIVE".equalsIgnoreCase(supplier.getStatus())) {
            throw new BadRequestException("ไม่สามารถแก้ไขซัพพลายเออร์ที่ถูกลบหรือปิดการใช้งานแล้ว");
        }

        var profile = supplierProfileRepository.findById(supplierUserId)
                .orElseGet(() -> {
                    var p = new com.pkshop.domain.supplier.entity.SupplierProfile();
                    p.setUserId(supplierUserId);
                    p.setCreatedAt(LocalDateTime.now());
                    return p;
                });

        supplier.setFullName(request.contactName().trim());
        if (blankToNull(request.contactPhone()) != null) {
            supplier.setPhone(request.contactPhone().trim());
        }
        userRepository.save(supplier);

        profile.setCompanyName(request.companyName().trim());
        profile.setContactName(request.contactName().trim());
        profile.setContactEmail(blankToNull(request.contactEmail()) != null
                ? request.contactEmail().trim().toLowerCase(Locale.ROOT)
                : supplier.getEmail());
        profile.setContactPhone(blankToNull(request.contactPhone()) != null
                ? request.contactPhone().trim()
                : supplier.getPhone());
        supplierProfileRepository.save(profile);

        return new AdminSupplierListResponse(
                supplier.getId(),
                profile.getCompanyName(),
                profile.getContactName(),
                profile.getContactEmail(),
                profile.getCountry(),
                profile.getContactPhone(),
                profile.getCreatedAt()
        );
    }

    @Transactional
    public void deleteSupplier(Long supplierUserId) {
        User supplier = requireSupplier(supplierUserId);
        supplierProfileRepository.deleteById(supplierUserId);
        supplier.setStatus("INACTIVE");
        userRepository.save(supplier);

        List<SupplierProduct> activeOffers = supplierProductRepository
                .findBySupplierUser_IdAndActiveTrueOrderByProduct_NameAsc(supplierUserId);
        for (SupplierProduct offer : activeOffers) {
            offer.setActive(false);
        }
    }

    @Transactional(readOnly = true)
    public List<SupplierProductResponse> listSupplierProducts(Long supplierUserId, String keyword) {
        requireSupplier(supplierUserId);
        String search = keyword == null ? "" : keyword.trim().toLowerCase(Locale.ROOT);
        return supplierProductRepository
                .findBySupplierUser_IdAndActiveTrueOrderByProduct_NameAsc(supplierUserId)
                .stream()
                .filter(offer -> search.isEmpty()
                        || contains(offer.getProduct().getName(), search)
                        || contains(offer.getProduct().getSku(), search)
                        || contains(offer.getSupplierSku(), search))
                .map(this::toSupplierProductResponse)
                .toList();
    }

    @Transactional
    public SupplierProductResponse addSupplierProduct(Long supplierUserId, CreateSupplierProductRequest request) {
        User supplier = requireSupplier(supplierUserId);
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new BadRequestException("ไม่พบสินค้าใน catalog"));
        if (!Boolean.TRUE.equals(product.getIsActive())) {
            throw new BadRequestException("ไม่สามารถผูกสินค้าที่ปิดใช้งานแล้วได้");
        }

        int minOrderQty = request.minOrderQty() == null ? 1 : request.minOrderQty();
        if (minOrderQty < 1) {
            throw new BadRequestException("จำนวนสั่งซื้อขั้นต่ำต้องไม่น้อยกว่า 1");
        }

        SupplierProduct offer = supplierProductRepository
                .findBySupplierUser_IdAndProduct_Id(supplierUserId, product.getId())
                .orElseGet(SupplierProduct::new);
        offer.setSupplierUser(supplier);
        offer.setProduct(product);
        offer.setSupplierSku(blankToNull(request.supplierSku()));
        offer.setMinOrderQty(minOrderQty);
        offer.setActive(true);

        return toSupplierProductResponse(supplierProductRepository.save(offer));
    }

    @Transactional
    public void removeSupplierProduct(Long supplierUserId, Long productId) {
        requireSupplier(supplierUserId);
        SupplierProduct offer = supplierProductRepository
                .findBySupplierUser_IdAndProduct_Id(supplierUserId, productId)
                .orElseThrow(() -> new BadRequestException("ไม่พบสินค้าที่ผูกกับซัพพลายเออร์รายนี้"));
        offer.setActive(false);
    }

    private User requireSupplier(Long supplierUserId) {
        User supplier = userRepository.findById(supplierUserId)
                .orElseThrow(() -> new BadRequestException("ไม่พบซัพพลายเออร์"));
        boolean hasSupplierRole = supplier.getRoles().stream()
                .anyMatch(role -> "SUPPLIER".equalsIgnoreCase(role.getName()));
        if (!hasSupplierRole) {
            throw new BadRequestException("บัญชีนี้ไม่มี role SUPPLIER");
        }
        return supplier;
    }

    private SupplierProductResponse toSupplierProductResponse(SupplierProduct offer) {
        Product product = offer.getProduct();
        return new SupplierProductResponse(
                offer.getId(),
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getStockQty(),
                product.getPrice(),
                offer.getSupplierSku(),
                offer.getMinOrderQty()
        );
    }

    private boolean contains(String value, String search) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(search);
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}