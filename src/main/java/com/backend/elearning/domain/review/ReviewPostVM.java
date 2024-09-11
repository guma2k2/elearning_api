package com.backend.elearning.domain.review;

import jakarta.validation.constraints.NotNull;

public record ReviewPostVM(
        Long courseId,
        String content,
        @NotNull(message = "rating star must not be null")
        Integer ratingStar
) {
}
