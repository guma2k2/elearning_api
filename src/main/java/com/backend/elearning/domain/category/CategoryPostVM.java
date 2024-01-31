package com.backend.elearning.domain.category;

public record CategoryPostVM (
        Integer id,
        String name,
        String description,
        boolean isPublish,
        Integer parentId
) {
}
