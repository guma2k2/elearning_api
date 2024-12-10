package com.backend.elearning.domain.promotion;


import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.coupon.CouponPostVM;
import com.backend.elearning.domain.coupon.CouponVM;
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
public class PromotionController {

    private final PromotionService promotionService;


    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }


    @PostMapping("/admin/promotions")
    public ResponseEntity<PromotionVM> create (
            @RequestBody PromotionPostVM promotionPostVM
    ) {
        PromotionVM promotionVM = promotionService.create(promotionPostVM);
        return ResponseEntity.ok().body(promotionVM);
    }


    @PutMapping("/admin/promotions/{promotionId}")
    public ResponseEntity<PromotionVM> update (
            @RequestBody PromotionPostVM promotionPostVM,
            @PathVariable("promotionId") Long promotionId
    ) {
        PromotionVM promotionVM = promotionService.update(promotionPostVM, promotionId);
        return ResponseEntity.ok().body(promotionVM);
    }


    @PostMapping("/admin/add/course/{courseId}/to/promotion/{promotionId}")
    public ResponseEntity<Void> addCourse(@PathVariable("courseId") Long courseId,
                                          @PathVariable("promotionId") Long promotionId) {
        promotionService.addCourseToPromotion(promotionId, courseId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/admin/add/course/{courseId}/to/promotion/{promotionId}")
    public ResponseEntity<Void> removeCourse(@PathVariable("courseId") Long courseId,
                                          @PathVariable("courseId") Long promotionId) {

        promotionService.removeCourseFromPromotion(promotionId, courseId);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/admin/promotions/{promotionId}")
    public ResponseEntity<Void> remove(
                                             @PathVariable("courseId") Long promotionId) {

        promotionService.deletePromotion(promotionId);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/admin/promotions/paging")
    public ResponseEntity<PageableData<PromotionVM>> getPageable (
            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE) int pageSize
    ) {
        PageableData<PromotionVM> pageablePromotions = promotionService.getPageablePromotions(pageNum, pageSize);
        return ResponseEntity.ok().body(pageablePromotions);
    }
}
