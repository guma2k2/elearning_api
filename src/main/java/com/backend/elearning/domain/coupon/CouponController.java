package com.backend.elearning.domain.coupon;

import com.backend.elearning.domain.category.CategoryVM;
import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.course.CourseVM;
import com.backend.elearning.exception.ErrorVm;
import com.backend.elearning.utils.Constants;
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


    @PutMapping("/admin/coupons/{couponId}")
    public ResponseEntity<CouponVM> updateCoupon (
            @RequestBody CouponPostVM couponPostVM,
            @PathVariable("couponId") Long couponId
    ) {
        CouponVM coupon = couponService.updateCoupon(couponPostVM, couponId);
        return ResponseEntity.ok().body(coupon);
    }


    @GetMapping("/admin/coupons/paging")
    public ResponseEntity<PageableData<CouponVM>> getPageableCategory (
            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE) int pageSize
    ) {
        PageableData<CouponVM> pageableCoupons = couponService.getPageableCoupons(pageNum, pageSize);
        return ResponseEntity.ok().body(pageableCoupons);
    }


    @GetMapping("/coupons/code/{code}")
    public ResponseEntity<CouponVM> getCourseById (
            @PathVariable("code") String code
    ) {
        CouponVM couponVm = couponService.getByCode(code);
        return ResponseEntity.ok().body(couponVm);
    }

    @DeleteMapping("/admin/coupons/{id}")
    public ResponseEntity<Void> delete (
            @PathVariable("id") Long id
    ) {
        couponService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
