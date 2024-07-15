package com.backend.elearning.domain.cart;

import java.util.List;

public interface CartService {

    void addCourseToCart(Long courseId);
    void deleteCourseInCart(Long cartId);

    void updateCartBuyLater(Long cartId);
    List<CartListGetVM> listCartForUser();
}
