package com.backend.elearning.domain.coupon;

import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.category.CategoryVM;
import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
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
        if (checkExistCoupon(null, couponPostVM.code())) {
            throw new DuplicateException(Constants.ERROR_CODE.COUPON_CODE_DUPLICATED, couponPostVM.code());
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
        Coupon coupon = couponRepository.findByCode(code).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.COUPON_NOT_FOUND, code));
        if (LocalDateTime.now().isBefore(coupon.getStartTime()) || LocalDateTime.now().isAfter(coupon.getEndTime())) {
            throw new BadRequestException(Constants.ERROR_CODE.COUPON_IS_EXPIRED, code);
        }
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


    public boolean checkExistCoupon (Long id, String code) {
        return couponRepository.findByCodeAndId(code, id) > 0l;
    }

    @Override
    public CouponVM updateCoupon(CouponPostVM couponPostVM, Long couponId) {
        if (checkExistCoupon(couponId, couponPostVM.code())) {
            throw new DuplicateException(Constants.ERROR_CODE.COUPON_CODE_DUPLICATED, couponPostVM.code());
        }
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.COUPON_NOT_FOUND, couponId));
        coupon.setCode(couponPostVM.code());
        coupon.setDiscountPercent(couponPostVM.discountPercent());
        LocalDateTime startTime = DateTimeUtils.convertStringToLocalDateTime(couponPostVM.startTime(), DateTimeUtils.NORMAL_TYPE);
        LocalDateTime endTime = DateTimeUtils.convertStringToLocalDateTime(couponPostVM.endTime(), DateTimeUtils.NORMAL_TYPE);
        coupon.setStartTime(startTime);
        coupon.setEndTime(endTime);
        Coupon updatedCoupon = couponRepository.save(coupon);
        return CouponVM.fromModel(updatedCoupon);
    }

    @Override
    public void deleteById(Long couponId) {
        Coupon coupon = couponRepository.findByIdCustom(couponId).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.COUPON_NOT_FOUND, couponId));
        if (coupon.getOrders().size() > 0) {
            throw new BadRequestException("Coupon had order");
        }
        couponRepository.delete(coupon);
    }
}
