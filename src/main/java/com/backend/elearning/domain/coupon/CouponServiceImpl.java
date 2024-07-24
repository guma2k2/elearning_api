package com.backend.elearning.domain.coupon;

import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.category.CategoryVM;
import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.utils.DateTimeUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public CouponVM createCoupon(CouponPostVM couponPostVM) {
        if (couponRepository.findByCode(couponPostVM.code()).isPresent()) {
            throw new DuplicateException("");
        }
        LocalDateTime startTime = DateTimeUtils.convertStringToLocalDateTime(couponPostVM.startTime(), DateTimeUtils.NORMAL_TYPE);
        LocalDateTime endTime = DateTimeUtils.convertStringToLocalDateTime(couponPostVM.endTime(), DateTimeUtils.NORMAL_TYPE);
        Coupon newCoupon = Coupon.builder()
                .discountPercent(couponPostVM.discountPercent())
                .code(couponPostVM.code())
                .startTime(startTime)
                .endTime(endTime)
                .build();
        Coupon savedCoupon = couponRepository.saveAndFlush(newCoupon);
        return CouponVM.fromModel(savedCoupon);
    }

    @Override
    public CouponVM getByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code).orElseThrow();
        return CouponVM.fromModel(coupon);
    }

    @Override
    public PageableData<CouponVM> getPageableCoupons(int pageNum, int pageSize) {
        List<CouponVM> couponVMS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        Page<Coupon> categoryPage = couponRepository.findAll(pageable);
        List<Coupon> coupons = categoryPage.getContent();
        for (Coupon coupon : coupons) {
            couponVMS.add(CouponVM.fromModel(coupon));
        }

        return new PageableData(
                pageNum,
                pageSize,
                (int) categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                couponVMS
        );
    }
}
