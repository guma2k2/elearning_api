package com.backend.elearning.domain.coupon;

public record CouponPostVM(
        String code,
        int discountPercent,
        String startTime,
        String endTime
) {
}
