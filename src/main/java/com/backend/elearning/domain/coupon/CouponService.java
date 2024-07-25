package com.backend.elearning.domain.coupon;

import com.backend.elearning.domain.common.PageableData;

public interface CouponService {
    CouponVM createCoupon(CouponPostVM couponPostVM);
    CouponVM getByCode(String code);

    PageableData<CouponVM> getPageableCoupons(int pageNum, int pageSize);

    CouponVM updateCoupon(CouponPostVM couponPostVM, Long couponId);

    void deleteById(Long couponId);
}
