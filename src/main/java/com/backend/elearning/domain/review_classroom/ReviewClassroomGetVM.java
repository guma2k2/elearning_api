package com.backend.elearning.domain.review_classroom;

import com.backend.elearning.domain.review.Review;
import com.backend.elearning.utils.DateTimeUtils;

public record ReviewClassroomGetVM (
        Long id,
        String content,
        int ratingStar,
        String updatedAt
) {

    public static ReviewClassroomGetVM fromModel(ReviewClassroom review) {
        String updatedAt = review.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(review.getUpdatedAt()) : "";
        return new ReviewClassroomGetVM(review.getId(), review.getContent(), review.getRatingStar(), updatedAt);
    }
}
