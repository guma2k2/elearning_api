package com.backend.elearning.service;

import com.backend.elearning.domain.category.CategoryServiceImpl;
import com.backend.elearning.domain.coupon.*;
import com.backend.elearning.domain.order.Order;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.utils.DateTimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    private CouponServiceImpl couponService;

    @BeforeEach
    void beforeEach() {
        couponService = new CouponServiceImpl(couponRepository);
    }


    @Test
    void createCoupon_shouldReturnCouponVM_whenCouponIsCreatedSuccessfully() {
        // given
        CouponPostVM couponPostVM = new CouponPostVM("CODE123", 10, "2024-08-25 10:00:00", "2024-08-26 10:00:00");
        LocalDateTime startTime = LocalDateTime.of(2024, 8, 25, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 8, 26, 10, 0);

        when(couponRepository.findByCodeAndId(couponPostVM.code(), null)).thenReturn(0L);

        Coupon savedCoupon = Coupon.builder()
                .id(1L)
                .discountPercent(couponPostVM.discountPercent())
                .code(couponPostVM.code())
                .startTime(startTime)
                .endTime(endTime)
                .build();

        when(couponRepository.saveAndFlush(any(Coupon.class))).thenReturn(savedCoupon);

        // when
        CouponVM result = couponService.createCoupon(couponPostVM);

        // then
        assertEquals(savedCoupon.getId(), result.id());
        assertEquals(savedCoupon.getCode(), result.code());
        assertEquals(savedCoupon.getDiscountPercent(), result.discountPercent());
        verify(couponRepository, times(1)).saveAndFlush(any(Coupon.class));
    }

    @Test
    void createCoupon_shouldThrowDuplicateException_whenCouponCodeAlreadyExists() {
        // given
        CouponPostVM couponPostVM = new CouponPostVM("CODE123", 10, "2024-08-25 10:00:00", "2024-08-26 10:00:00");

        when(couponRepository.findByCodeAndId(couponPostVM.code(), null)).thenReturn(1L);

        // when & then
        assertThrows(DuplicateException.class, () -> couponService.createCoupon(couponPostVM));

        verify(couponRepository, never()).saveAndFlush(any(Coupon.class));
    }

    @Test
    void updateCoupon_shouldReturnUpdatedCouponVM_whenCouponIsUpdatedSuccessfully() {
        // given
        Long couponId = 1L;
        CouponPostVM couponPostVM = new CouponPostVM("NEWCODE", 15, "2024-09-01 10:00:00", "2024-09-02 10:00:00");
        LocalDateTime startTime = LocalDateTime.of(2024, 9, 1, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 9, 2, 10, 0);

        Coupon existingCoupon = Coupon.builder()
                .id(couponId)
                .code("OLDCODE")
                .discountPercent(10)
                .startTime(LocalDateTime.of(2024, 8, 25, 10, 0))
                .endTime(LocalDateTime.of(2024, 8, 26, 10, 0))
                .build();

        Coupon updatedCoupon = Coupon.builder()
                .id(couponId)
                .code(couponPostVM.code())
                .discountPercent(couponPostVM.discountPercent())
                .startTime(startTime)
                .endTime(endTime)
                .build();

        when(couponRepository.findByCodeAndId(couponPostVM.code(), couponId)).thenReturn(0L);
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(existingCoupon));
        when(couponRepository.save(existingCoupon)).thenReturn(updatedCoupon);

        // when
        CouponVM result = couponService.updateCoupon(couponPostVM, couponId);

        // then
        assertEquals(updatedCoupon.getId(), result.id());
        assertEquals(updatedCoupon.getCode(), result.code());
        assertEquals(updatedCoupon.getDiscountPercent(), result.discountPercent());
        verify(couponRepository, times(1)).save(existingCoupon);
    }

    @Test
    void deleteById_shouldDeleteCoupon_whenCouponHasNoOrders() {
        // given
        Long couponId = 1L;
        Coupon coupon = Coupon.builder()
                .id(couponId)
                .code("TESTCODE")
                .orders(Collections.emptyList()) // No orders
                .build();

        when(couponRepository.findByIdCustom(couponId)).thenReturn(Optional.of(coupon));

        // when
        couponService.deleteById(couponId);

        // then
        verify(couponRepository, times(1)).delete(coupon);
    }

    @Test
    void deleteById_shouldThrowBadRequestException_whenCouponHasOrders() {
        // given
        Long couponId = 1L;
        Order order = new Order(); // Mocked order object
        Coupon coupon = Coupon.builder()
                .id(couponId)
                .code("TESTCODE")
                .orders(Collections.singletonList(order)) // Coupon has an order
                .build();

        when(couponRepository.findByIdCustom(couponId)).thenReturn(Optional.of(coupon));

        // when & then
        assertThrows(BadRequestException.class, () -> couponService.deleteById(couponId));
        verify(couponRepository, never()).delete(coupon); // Ensure delete is not called
    }

}
