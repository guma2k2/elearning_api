package com.backend.elearning.domain.review;

public record ReviewStatusPostVM(
        ReviewStatus status,
        String reason
) {
}
