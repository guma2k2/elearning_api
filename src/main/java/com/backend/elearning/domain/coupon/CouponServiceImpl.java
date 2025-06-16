package com.backend.elearning.domain.coupon;

import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.category.CategoryVM;
import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import com.backend.elearning.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public CouponVM createCoupon(CouponPostVM couponPostVM) {
        if (couponRepository.findByCodeAndId(couponPostVM.code(), null) > 0l) {
            throw new DuplicateException(Constants.ERROR_CODE.COUPON_CODE_DUPLICATED, couponPostVM.code());
        }
        log.info("received couponPostVM: {}", couponPostVM);
        LocalDateTime startTime = DateTimeUtils.convertStringToLocalDateTime(couponPostVM.startTime(), DateTimeUtils.NORMAL_TYPE);
        LocalDateTime endTime = DateTimeUtils.convertStringToLocalDateTime(couponPostVM.endTime(), DateTimeUtils.NORMAL_TYPE);

        if (startTime.isAfter(endTime)) {
            throw new BadRequestException("Start time must be before end time.");
        }
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
        log.info("received code of coupon service: {}", code);
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



    @Override
    public CouponVM updateCoupon(CouponPostVM couponPostVM, Long couponId) {
        log.info("received couponPostVM: {}", couponPostVM);
        if (couponRepository.findByCodeAndId(couponPostVM.code(), couponId) > 0l) {
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
        log.info("received couponId: {}", couponId);
        Coupon coupon = couponRepository.findByIdCustom(couponId).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.COUPON_NOT_FOUND, couponId));
        if (coupon.getOrders().size() > 0) {
            throw new BadRequestException("Coupon had orders");
        }
        couponRepository.delete(coupon);
    }
}
