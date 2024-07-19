package com.backend.elearning.domain.review;


import com.backend.elearning.domain.common.PageableData;

import java.util.List;

public interface ReviewService {

    ReviewVM createReviewForProduct(ReviewPostVM reviewPost);

    PageableData<ReviewVM> getByMultiQuery(Long courseId, Integer pageNum, int pageSize, Integer ratingStar, String sortDir);

    ReviewVM updateReview(ReviewPostVM reviewPostVM, Long reviewId);

    List<Review> findByCourseId(Long courseId);
}
