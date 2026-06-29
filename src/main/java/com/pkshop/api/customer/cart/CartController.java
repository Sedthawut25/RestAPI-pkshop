package com.pkshop.api.customer.cart;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.sales.entity.CartItem;
import com.pkshop.domain.user.entity.User;
import com.pkshop.domain.user.repository.UserRepository;
import com.pkshop.dto.customer.cart.AddToCartRequest;
import com.pkshop.dto.customer.cart.CartItemResponse;
import com.pkshop.dto.customer.cart.UpdateCartQtyRequest;
import com.pkshop.service.cart.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/customer/cart")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepo;

    public CartController(CartService cartService, UserRepository userRepo) {
        this.cartService = cartService;
        this.userRepo = userRepo;
    }

    private User currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private CartItemResponse map(CartItem it) {
        var p = it.getProduct();

        System.out.println("====== DEBUG CART ITEM ======");
        System.out.println("CartItem ID: " + it.getId() + " | Qty: " + it.getQty());
        System.out.println("Product Object: " + p);
        if (p != null) {
            System.out.println("Product ID: " + p.getId() + " | Name: " + p.getName() + " | Price: " + p.getPrice());
        }
        System.out.println("=============================");

        BigDecimal unit = (p != null && p.getPrice() != null) ? p.getPrice() : BigDecimal.ZERO;
        int qty = it.getQty() == null ? 0 : it.getQty();
        BigDecimal line = unit.multiply(BigDecimal.valueOf(qty));

        return new CartItemResponse(
                it.getId(),
                p != null ? p.getId() : null,
                p != null ? p.getSku() : null,
                p != null ? p.getName() : null,
                unit,
                qty,
                line,
                p != null ? p.getStockQty() : null,
                p != null ? p.getImageUrl() : null
        );
    }

    @GetMapping
    public ApiResponse<List<CartItemResponse>> myCart() {
        var items = cartService.getMyCart(currentUser()).stream().map(this::map).toList();
        return ApiResponse.ok("Cart", items);
    }

    @PostMapping("/items")
    public ApiResponse<CartItemResponse> add(@Valid @RequestBody AddToCartRequest req) {
        var it = cartService.addToCart(currentUser(), req.productId(), req.qty());
        return ApiResponse.ok("Added", map(it));
    }

    @PutMapping("/items/{productId}")
    public ApiResponse<CartItemResponse> updateQty(@PathVariable Long productId, @Valid @RequestBody UpdateCartQtyRequest req) {
        var it = cartService.updateQty(currentUser(), productId, req.qty());
        return ApiResponse.ok("Updated", map(it));
    }

    @DeleteMapping("/items/{productId}")
    public ApiResponse<Void> remove(@PathVariable Long productId) {
        cartService.removeItem(currentUser(), productId);
        return ApiResponse.ok("Removed", null);
    }

    @DeleteMapping
    public ApiResponse<Void> clear() {
        cartService.clear(currentUser());
        return ApiResponse.ok("Cleared", null);
    }
}