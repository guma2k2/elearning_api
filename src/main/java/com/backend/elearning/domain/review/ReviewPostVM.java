package com.backend.elearning.domain.review;

public record ReviewPostVM(
        Long courseId,
        String content,
        int ratingStar
) {
}
