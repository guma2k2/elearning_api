package com.backend.elearning.domain.coupon;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public record CouponVM(Long id,
                       int discountPercent,
                       String code,
                       String startTime,
                       String endTime
) {

    public static CouponVM fromModel(Coupon coupon) {
        return new CouponVM(coupon.getId(), coupon.getDiscountPercent(), coupon.getCode(), coupon.getStartTime().toString(), coupon.getEndTime().toString());
    }
}
