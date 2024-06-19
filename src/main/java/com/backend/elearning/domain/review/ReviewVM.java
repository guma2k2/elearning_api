package com.backend.elearning.domain.review;

import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserGetVM;

public record ReviewVM(
        Long id,
        String content,
        int rating,
        UserGetVM student,
        String created_at,
        String updated_at
) {

    public static ReviewVM fromModel(Review review) {
        User user = review.getUser();
        String updatedAt = review.getUpdatedAt() == null ? review.getCreatedAt().toString() : review.getUpdatedAt().toString();
        return new ReviewVM(review.getId(), review.getContent(), review.getRatingStar(),
                UserGetVM.fromModel(user), review.getCreatedAt().toString(), updatedAt);
    }
}
