package com.backend.elearning.domain.review;


import com.backend.elearning.domain.common.PageableData;

import java.util.List;

public interface ReviewService {

    void createReviewForProduct(ReviewPostVM reviewPost);

    PageableData<ReviewVM> getByMultiQuery(Long courseId, Integer pageNum, int pageSize, Integer ratingStar, String sortDir);
}
