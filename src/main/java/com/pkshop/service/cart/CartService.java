package com.pkshop.service.cart;

import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.domain.sales.entity.Cart;
import com.pkshop.domain.sales.entity.CartItem;
import com.pkshop.domain.sales.repository.CartItemRepository;
import com.pkshop.domain.sales.repository.CartRepository;
import com.pkshop.domain.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final ProductRepository productRepo;

    public CartService(CartRepository cartRepo, CartItemRepository cartItemRepo, ProductRepository productRepo) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.productRepo = productRepo;
    }

    private Cart requireActiveCart(User customer) {
        return cartRepo.findByCustomerIdAndStatus(customer.getId(), Cart.Status.ACTIVE)
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setCustomer(customer);
                    c.setStatus(Cart.Status.ACTIVE);
                    c.setCreatedAt(Instant.now());
                    return cartRepo.save(c);
                });
    }

    public List<CartItem> getMyCart(User customer) {
        // fetch join product แล้ว รูป/ชื่อ/ราคาออกชัวร์
        return cartItemRepo.findMyCartItems(customer.getId(), Cart.Status.ACTIVE);
    }

    public CartItem addToCart(User customer, Long productId, Integer qty) {
        if (qty == null || qty <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "qty must be > 0");
        }

        Product p = productRepo.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        if (Boolean.FALSE.equals(p.getIsActive())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product inactive");
        }

        int stock = p.getStockQty() == null ? 0 : p.getStockQty();
        if (stock <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Out of stock");
        }

        Cart cart = requireActiveCart(customer);

        CartItem ci = cartItemRepo.findMyItem(customer.getId(), Cart.Status.ACTIVE, productId).orElse(null);
        if (ci == null) {
            ci = new CartItem();
            ci.setCart(cart);
            ci.setProduct(p);
            ci.setQty(0);
        }

        int newQty = (ci.getQty() == null ? 0 : ci.getQty()) + qty;
        if (newQty > stock) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough stock");
        }

        ci.setQty(newQty);
        return cartItemRepo.save(ci);
    }

    public CartItem updateQty(User customer, Long productId, Integer qty) {
        if (qty == null || qty < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "qty must be >= 0");
        }

        CartItem ci = cartItemRepo.findMyItem(customer.getId(), Cart.Status.ACTIVE, productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found"));

        Product p = ci.getProduct();
        int stock = (p != null && p.getStockQty() != null) ? p.getStockQty() : 0;

        if (qty == 0) {
            cartItemRepo.delete(ci);
            return ci;
        }

        if (qty > stock) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough stock");
        }

        ci.setQty(qty);
        return cartItemRepo.save(ci);
    }

    public void removeItem(User customer, Long productId) {
        CartItem ci = cartItemRepo.findMyItem(customer.getId(), Cart.Status.ACTIVE, productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found"));
        cartItemRepo.delete(ci);
    }

    public void clear(User customer) {
        // ลบ item ทั้งหมดใน active cart
        List<CartItem> items = cartItemRepo.findMyCartItems(customer.getId(), Cart.Status.ACTIVE);
        cartItemRepo.deleteAll(items);
    }
}