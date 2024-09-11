package com.backend.elearning.domain.coupon;

import jakarta.validation.constraints.NotEmpty;

public record CouponPostVM(

        @NotEmpty(message = "coupon code must not be empty")
        String code,
        int discountPercent,
        String startTime,
        String endTime
) {
}
