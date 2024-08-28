package com.backend.elearning.repository;

import com.backend.elearning.TestConfig;
import com.backend.elearning.domain.coupon.Coupon;
import com.backend.elearning.domain.coupon.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TestConfig.class})
public class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        // Clearing the repository before each test
        couponRepository.deleteAll();

        // Setting up test data
        Coupon coupon1 = Coupon.builder()
                .discountPercent(10)
                .code("DISCOUNT10")
                .startTime(LocalDateTime.now().minusDays(1))
                .endTime(LocalDateTime.now().plusDays(1))
                .build();

        Coupon coupon2 = Coupon.builder()
                .discountPercent(20)
                .code("DISCOUNT20")
                .startTime(LocalDateTime.now().minusDays(1))
                .endTime(LocalDateTime.now().plusDays(1))
                .build();

        couponRepository.saveAll(List.of(coupon1, coupon2));
    }

    @Test
    void testFindByCodeAndId_WithExistingCodeAndDifferentId() {
        // Given
        String code = "DISCOUNT10";
        Long id = 999L;  // ID that doesn't exist in the database

        // When
        long count = couponRepository.findByCodeAndId(code, id);

        // Then
        assertEquals(1L, count, "Should return 1 since the code exists but ID does not match");
    }

    @Test
    void testFindByCodeAndId_WithExistingCodeAndSameId() {
        // Given
        Coupon existingCoupon = couponRepository.findAll().get(0);
        String code = existingCoupon.getCode();
        Long id = existingCoupon.getId();

        // When
        long count = couponRepository.findByCodeAndId(code, id);

        // Then
        assertEquals(0L, count, "Should return 0 since the code exists but the ID matches");
    }

    @Test
    void testFindByCodeAndId_WithNonExistingCode() {
        // Given
        String code = "NON_EXISTING_CODE";
        Long id = null;

        // When
        long count = couponRepository.findByCodeAndId(code, id);

        // Then
        assertEquals(0L, count, "Should return 0 since the code does not exist");
    }

    @Test
    void testFindByIdCustom_WithExistingId() {
        // Given
        Coupon coupon = couponRepository.findAll().get(0);
        Long id = coupon.getId();

        // When
        Optional<Coupon> result = couponRepository.findByIdCustom(id);

        // Then
        assertTrue(result.isPresent(), "Coupon should be found with the given ID");
        assertEquals("DISCOUNT10", result.get().getCode(), "The coupon code should match");
    }

    @Test
    void testFindByIdCustom_WithNonExistingId() {
        // Given
        Long id = 999L;  // Non-existing ID

        // When
        Optional<Coupon> result = couponRepository.findByIdCustom(id);

        // Then
        assertFalse(result.isPresent(), "No coupon should be found with a non-existing ID");
    }
}
