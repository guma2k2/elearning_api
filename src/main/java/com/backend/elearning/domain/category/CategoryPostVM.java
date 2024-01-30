package com.backend.elearning.domain.category;

public record CategoryPostVM (
        String name,
        String description,
        Integer parentId
) {
}
