package com.backend.elearning.domain.cart;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts/add-to-cart/course/{courseId}")
    public ResponseEntity<CartListGetVM> addToCart(@PathVariable("courseId") Long courseId) {
        CartListGetVM cartListGetVM = cartService.addCourseToCart(courseId);
        return ResponseEntity.ok().body(cartListGetVM);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartListGetVM>> listCartForCustomer() {
        List<CartListGetVM> cartListGetVMS = cartService.listCartForUser();
        return ResponseEntity.ok().body(cartListGetVMS);
    }

    @DeleteMapping("/carts/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("cartId") Long cartId) {
        cartService.deleteCourseInCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/carts/{cartId}")
    public ResponseEntity<Void> updateCartStatus(@PathVariable("cartId") Long cartId) {
        cartService.updateCartBuyLater(cartId);
        return ResponseEntity.noContent().build();
    }
}
