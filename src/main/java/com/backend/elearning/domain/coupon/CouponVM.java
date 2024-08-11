package com.backend.elearning.domain.coupon;

import com.backend.elearning.utils.DateTimeUtils;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

public record CouponVM(Long id,
                       int discountPercent,
                       String code,
                       String startTime,
                       String endTime
) {

    public static CouponVM fromModel(Coupon coupon) {
        String start = coupon.getStartTime() != null ?
                DateTimeUtils.convertLocalDateTimeToString(coupon.getStartTime()) : "";
        String end = coupon.getEndTime() != null ?
                DateTimeUtils.convertLocalDateTimeToString(coupon.getEndTime()) : "";
        return new CouponVM(coupon.getId(), coupon.getDiscountPercent(), coupon.getCode(),
                start,
                end);
    }
}
