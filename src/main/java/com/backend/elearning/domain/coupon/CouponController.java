package com.backend.elearning.domain.coupon;

import com.backend.elearning.domain.course.CoursePostVM;
import com.backend.elearning.domain.course.CourseVM;
import com.backend.elearning.exception.ErrorVm;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CouponController {

    private final CouponService couponService;


    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }


    @PostMapping("/admin/coupons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
            @Content(schema = @Schema(implementation = CourseVM.class))),
            @ApiResponse(responseCode = "409", description = "Duplicated title", content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
    })
    public ResponseEntity<CouponVM> createCoupon (
            @RequestBody CouponPostVM couponPostVM
    ) {
        CouponVM coupon = couponService.createCoupon(couponPostVM);
        return ResponseEntity.status(HttpStatus.CREATED).body(coupon);
    }


    @GetMapping("/coupons/{code}")
    public ResponseEntity<CouponVM> getCourseById (
            @PathVariable("code") String code
    ) {
        CouponVM couponVm = couponService.getByCode(code);
        return ResponseEntity.ok().body(couponVm);
    }
}
