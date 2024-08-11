package com.backend.elearning.domain.review;


import com.backend.elearning.utils.DateTimeUtils;

public record ReviewGetVM(
        Long id,
        String content,
        int ratingStar,
        String updated_at
) {
    public static ReviewGetVM fromModel(Review review) {
        String updatedAt = review.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(review.getUpdatedAt()) : "";
        return new ReviewGetVM(review.getId(), review.getContent(), review.getRatingStar(), updatedAt);
    }
}
