package com.backend.elearning.domain.order;

import java.util.List;

public record OrderPostDto(
        String couponCode,
        List<OrderDetailPostDto> orderDetails
) {
}
