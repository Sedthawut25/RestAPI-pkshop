package com.pkshop.service.admin;

import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.domain.sales.entity.CustomerClaim;
import com.pkshop.domain.sales.entity.Order;
import com.pkshop.domain.sales.entity.OrderItem;
import com.pkshop.domain.sales.repository.CustomerClaimRepository;
import com.pkshop.domain.sales.repository.OrderItemRepository; // 👉 เพิ่ม Import
import com.pkshop.domain.sales.repository.OrderRepository;
import com.pkshop.dto.admin.claim.UpdateClaimStatusRequest;
import com.pkshop.service.customer.checkout.StripeCheckoutService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class AdminClaimService {

    private final CustomerClaimRepository customerClaimRepository;
    private final OrderRepository orderRepository;
    private final StripeCheckoutService stripeCheckoutService;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository; // 👉 เพิ่มตัวแปร Repository

    public AdminClaimService(CustomerClaimRepository customerClaimRepository,
                             OrderRepository orderRepository,
                             StripeCheckoutService stripeCheckoutService,
                             ProductRepository productRepository,
                             OrderItemRepository orderItemRepository) { // 👉 เพิ่มใน Constructor
        this.customerClaimRepository = customerClaimRepository;
        this.orderRepository = orderRepository;
        this.stripeCheckoutService = stripeCheckoutService;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository; // 👉 กำหนดค่าเริ่มต้น
    }

    public Page<CustomerClaim> listClaims(String keyword, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return customerClaimRepository.findAll(pageable);
    }

    public CustomerClaim getClaimDetail(Long id) {
        return customerClaimRepository.findById(id).orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลการเคลมของ ID: " + id));
    }

    @Transactional(rollbackFor = Exception.class) // มั่นใจได้ว่าถ้า Stripe พัง DB จะ Rollback
    public void updateClaimStatus(Long id, UpdateClaimStatusRequest request) throws Exception {
        CustomerClaim claim = customerClaimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลการเคลมของ ID: " + id));

        claim.setStatus(request.status());
        claim.setAdminComment(request.adminRemark());

        //เงื่อนไขในการเคลม
        if ("APPROVED".equals(request.status())) {

            //เงื่อนไขในการเลือกคืนเงิน
            if ("RETURN_MONEY".equals(claim.getClaimType())) {

                Order order = orderRepository.findById(claim.getOrderId())
                        .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์หมายเลข: " + claim.getOrderId() + " ที่ผูกกับใบเคลมนี้"));

                String paymentIntentId = order.getPaymentIntentId();

                if (paymentIntentId == null || paymentIntentId.isBlank()) {
                    throw new RuntimeException("ไม่พบ Payment Intent ID สำหรับออเดอร์นี้ ไม่สามารถคืนเงินได้");
                }

                OrderItem claimedItem = order.getItems().stream()
                        .filter(item -> item.getProduct().getId().equals(claim.getProductId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("ไม่พบสินค้าที่ต้องการในเคลมในออเดอร์นี้"));

                BigDecimal unitPrice = claimedItem.getUnitPrice();
                BigDecimal claimQty = BigDecimal.valueOf(claim.getQuantity());
                BigDecimal amountToRefund = unitPrice.multiply(claimQty);

                if (amountToRefund == null || amountToRefund.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new RuntimeException("ยอดเงินที่ไม่ถูกต้อง ไม่สามารถดำเนินการคืนเงินให้คุณได้");
                }

                claim.setRefundAmount(amountToRefund);

                stripeCheckoutService.refundOrder(paymentIntentId, amountToRefund);

                order.setStatus("PARTIALLY_REFUNDED");
                orderRepository.save(order);
            }

            //เงื่อนไขในการเลือกเปลี่ยนสินค้า
            else if ("EXCHANGE".equals(claim.getClaimType())){

                //ดึงสินค้าข้อมูลสินค้าที่ลูกค้าต้องการเปลี่ยน
                Product product = productRepository.findById(claim.getProductId())
                        .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลสินค้าชิ้นนี้ในคลัง"));

                //เงื่อนไขในการตรวจสอบสินค้ามีเพียงพอในการเปลี่ยนให้ลูกค้ามั้ย
                if (product.getStockQty() < claim.getQuantity()) {
                    throw new RuntimeException("ไม่สามารถอนุมัติได้เนื่องจากสินค้าในคลังไม่เพียงพอในการเปลี่ยน สินค้าคงเหลือ: " + product.getStockQty()+ " ชิ้น");
                }

                //ลบจำนวนสินค้าเมื่อทำการเปลี่ยน
                product.setStockQty(product.getStockQty() - claim.getQuantity());
                productRepository.save(product);

                //ดึงออเดอร์เดิม
                Order originalOrder = orderRepository.findById(claim.getOrderId())
                        .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์หมายเลข: " + claim.getOrderId()));

                //สร้างออเดอร์ใหม่ 0 บาท สำหรับส่งเคลม
                Order replacementOrder = new Order();
                replacementOrder.setOrderNumber(originalOrder.getOrderNumber() + "-EX");
                replacementOrder.setCustomer(originalOrder.getCustomer());
                replacementOrder.setStatus("PENDING"); // รอแอดมินจัดส่ง

                // ยอดเงินเป็น 0
                replacementOrder.setSubtotal(BigDecimal.ZERO);
                replacementOrder.setShippingFee(BigDecimal.ZERO);
                replacementOrder.setPromotionDiscount(BigDecimal.ZERO);
                replacementOrder.setGrandTotal(BigDecimal.ZERO);
                replacementOrder.setShippingAddress(originalOrder.getShippingAddress());
                replacementOrder.setCreatedAt(java.time.LocalDateTime.now());

                // บันทึกออเดอร์ใหม่ลงฐานข้อมูลก่อน
                replacementOrder = orderRepository.save(replacementOrder);

                //OrderItem สำหรับออเดอร์ใหม่
                OrderItem claimItem = new OrderItem();
                claimItem.setOrder(replacementOrder);
                claimItem.setProduct(product);
                claimItem.setQty(claim.getQuantity());
                claimItem.setUnitPrice(BigDecimal.ZERO);
                claimItem.setLineTotal(BigDecimal.ZERO);
                claimItem.setProductNameSnapshot(product.getName() + " (เคลมเปลี่ยนสินค้า)");
                claimItem.setProductImageSnapshot(product.getImageUrl());

                // 👉 บันทึกรายการสินค้าลงในฐานข้อมูล
                orderItemRepository.save(claimItem);

                //ให้ใบเคลมจดจำ ID ของออเดอร์ใหม่ไว้
                claim.setReplacementOrderId(replacementOrder.getId());
            }
        }

        customerClaimRepository.save(claim);
    }
}