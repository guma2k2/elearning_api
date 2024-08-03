package com.backend.elearning.domain.review;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.coupon.CouponVM;
import com.backend.elearning.utils.Constants;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/reviews")
    public ResponseEntity<ReviewVM> createReview(@Valid @RequestBody ReviewPostVM reviewPostVM){
        ReviewVM review = reviewService.createReviewForProduct(reviewPostVM);
        return ResponseEntity.ok().body(review);
    }


    @PutMapping("/reviews/{id}")
    public ResponseEntity<ReviewVM> updateReview(@Valid @RequestBody ReviewPostVM reviewPostVM, @PathVariable("id") Long reviewId){
        ReviewVM updatedReview = reviewService.updateReview(reviewPostVM, reviewId);
        return ResponseEntity.ok().body(updatedReview);
    }


    @PutMapping("/admin/reviews/{id}/status/{status}")
    public ResponseEntity<Void> updateReview(@PathVariable("status") boolean status, @PathVariable("id") Long reviewId){
        reviewService.updateStatusReview(status, reviewId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/admin/reviews/paging")
    public ResponseEntity<PageableData<ReviewGetListVM>> getPageableCategory (
            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        PageableData<ReviewGetListVM> pageableReviews = reviewService.getPageableReviews(pageNum, pageSize, keyword);
        return ResponseEntity.ok().body(pageableReviews);
    }

    @GetMapping("/reviews/search/{courseId}")
    public ResponseEntity<PageableData<ReviewVM>> getByBaseProductId(
            @PathVariable("courseId") Long courseId,

            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNum,

            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE_REVIEW, required = false) int pageSize,

            @RequestParam(value = "ratingStar", required = false) Integer ratingStar,

            @RequestParam(value = "sortDir", defaultValue = Constants.PageableConstant.DEFAULT_SORT_DIR, required = false) String sortDir
    ) {
        return ResponseEntity.ok().body(reviewService.getByMultiQuery(courseId, pageNum, pageSize, ratingStar,sortDir));
    }


}
