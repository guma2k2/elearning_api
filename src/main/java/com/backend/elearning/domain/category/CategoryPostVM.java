package com.backend.elearning.domain.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryPostVM (
        Integer id,
        @NotBlank(message = "name must not be blank")
        String name,
        String description,
        boolean isPublish,
        Integer parentId
) {
}
