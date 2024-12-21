package com.backend.elearning.domain.review;


import com.backend.elearning.utils.DateTimeUtils;

public record ReviewGetVM(
        Long id,
        String content,
        int ratingStar,
        String status,
        String updatedAt,
        String reason
) {
    public static ReviewGetVM fromModel(Review review) {
        String updatedAt = review.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(review.getUpdatedAt()) : "";
        String reason = review.getReasonRefused() != null ? review.getReasonRefused() : "";
        return new ReviewGetVM(review.getId(), review.getContent(), review.getRatingStar(),review.getStatus().name(),
                updatedAt,
                reason);
    }
}
