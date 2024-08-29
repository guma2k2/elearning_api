package com.backend.elearning.controller;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.coupon.CouponController;
import com.backend.elearning.domain.coupon.CouponPostVM;
import com.backend.elearning.domain.coupon.CouponService;
import com.backend.elearning.domain.coupon.CouponVM;
import com.backend.elearning.security.JWTUtil;
import com.backend.elearning.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = CouponController.class,
        excludeAutoConfiguration = {
                UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class
        }
)
public class CouponControllerTest {
    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CouponService couponService;

    @Test
    void createCoupon_ShouldReturnCreated_WhenCouponIsCreated() throws Exception {
        // Given
        CouponPostVM couponPostVM = new CouponPostVM("DISCOUNT20", 20, "2024-09-01T00:00:00", "2024-09-30T23:59:59");
        CouponVM couponVM = new CouponVM(1L, 20, "DISCOUNT20", "2024-09-01T00:00:00", "2024-09-30T23:59:59");

        when(couponService.createCoupon(any(CouponPostVM.class))).thenReturn(couponVM);

        // When & Then
        mockMvc.perform(post("/api/v1/admin/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(couponPostVM)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("DISCOUNT20"))
                .andExpect(jsonPath("$.discountPercent").value(20));
    }

    @Test
    void updateCoupon_ShouldReturnOk_WhenCouponIsUpdated() throws Exception {
        // Given
        Long couponId = 1L;
        CouponPostVM couponPostVM = new CouponPostVM("DISCOUNT25", 25, "2024-09-01T00:00:00", "2024-09-30T23:59:59");
        CouponVM updatedCouponVM = new CouponVM(couponId, 25, "DISCOUNT25", "2024-09-01T00:00:00", "2024-09-30T23:59:59");

        when(couponService.updateCoupon(any(CouponPostVM.class), eq(couponId))).thenReturn(updatedCouponVM);

        // When & Then
        mockMvc.perform(put("/api/v1/admin/coupons/{couponId}", couponId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(couponPostVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(couponId))
                .andExpect(jsonPath("$.code").value("DISCOUNT25"))
                .andExpect(jsonPath("$.discountPercent").value(25));
    }

    @Test
    void getPageableCategory_ShouldReturnPageableCoupons_WhenValidRequest() throws Exception {
        // Given
        PageableData<CouponVM> pageableCoupons = new PageableData<>(1, 10, 100, 10, List.of(
                new CouponVM(1L, 20, "DISCOUNT20", "2024-09-01T00:00:00", "2024-09-30T23:59:59")
        ));

        when(couponService.getPageableCoupons(anyInt(), anyInt())).thenReturn(pageableCoupons);

        // When & Then
        mockMvc.perform(get("/api/v1/admin/coupons/paging")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].code").value("DISCOUNT20"))
                .andExpect(jsonPath("$.totalElements").value(100));
    }

    @Test
    void deleteCoupon_ShouldReturnNoContent_WhenCouponIsDeleted() throws Exception {
        // Given
        Long couponId = 1L;
        doNothing().when(couponService).deleteById(couponId);

        // When & Then
        mockMvc.perform(delete("/api/v1/admin/coupons/{id}", couponId))
                .andExpect(status().isNoContent());
    }

}
