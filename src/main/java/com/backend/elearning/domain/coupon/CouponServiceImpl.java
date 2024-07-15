package com.backend.elearning.domain.coupon;

import com.backend.elearning.utils.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public CouponVM createCoupon(CouponPostVM couponPostVM) {
        Coupon coupon = couponRepository.findByCode(couponPostVM.code()).orElseThrow();
        LocalDateTime startTime = DateTimeUtils.convertStringToLocalDateTime(couponPostVM.startTime());
        LocalDateTime endTime = DateTimeUtils.convertStringToLocalDateTime(couponPostVM.endTime());
        Coupon newCoupon = Coupon.builder()
                .discountPercent(couponPostVM.discountPercent())
                .code(couponPostVM.code())
                .startTime(startTime)
                .endTime(endTime)
                .build();
        Coupon savedCoupon = couponRepository.saveAndFlush(newCoupon);
        return CouponVM.fromModel(coupon);
    }

    @Override
    public CouponVM getByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code).orElseThrow();
        return CouponVM.fromModel(coupon);
    }
}
