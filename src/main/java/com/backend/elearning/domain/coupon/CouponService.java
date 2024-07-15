package com.backend.elearning.domain.coupon;

public interface CouponService {
    CouponVM createCoupon(CouponPostVM couponPostVM);
    CouponVM getByCode(String code);
}
