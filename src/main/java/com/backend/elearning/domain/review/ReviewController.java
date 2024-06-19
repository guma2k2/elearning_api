package com.backend.elearning.domain.review;

import com.backend.elearning.domain.common.PageableData;
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
    public ResponseEntity<Void> createReview(@Valid @RequestBody ReviewPostVM reviewPostVM){
        reviewService.createReviewForProduct(reviewPostVM);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reviews/search")
    public ResponseEntity<PageableData<ReviewVM>> getByBaseProductId(
            @PathVariable("courseId") Long courseId,

            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNum,

            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,

            @RequestParam(value = "ratingStar", required = false) Integer ratingStar,

            @RequestParam(value = "sortDir", defaultValue = Constants.PageableConstant.DEFAULT_SORT_DIR, required = false) String sortDir
    ) {
        return ResponseEntity.ok().body(reviewService.getByMultiQuery(courseId, pageNum, pageSize, ratingStar,sortDir));
    }


}
