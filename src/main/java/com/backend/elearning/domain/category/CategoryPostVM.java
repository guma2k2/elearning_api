package com.backend.elearning.domain.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CategoryPostVM (
        Integer id,
        @NotEmpty(message = "name must not be empty")
        String name,
        String description,
        boolean isPublish,
        Integer parentId
) {
}
