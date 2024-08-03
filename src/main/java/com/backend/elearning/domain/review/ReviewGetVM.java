package com.backend.elearning.domain.review;


public record ReviewGetVM(
        Long id,
        String content,
        int ratingStar,
        String updated_at
) {
    public static ReviewGetVM fromModel(Review review) {
        String updatedAt = review.getUpdatedAt() != null ? review.getUpdatedAt().toString() : "";
        return new ReviewGetVM(review.getId(), review.getContent(), review.getRatingStar(), updatedAt);
    }
}
