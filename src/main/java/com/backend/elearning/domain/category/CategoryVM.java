package com.backend.elearning.domain.category;

import com.backend.elearning.utils.DateTimeUtils;

public record CategoryVM(int id,
                         String name,
                         String description,
                         boolean isPublish,
                         String createdAt,
                         String updatedAt,
                         int parentId
) {
    public static CategoryVM fromModel(Category category) {
        Category parent  = category.getParent();
        int parentId = parent != null ? parent.getId() : -1;
        String createdAt = category.getCreatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(category.getCreatedAt()) : "";
        String updatedAt = category.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(category.getUpdatedAt()) : "";

        return new CategoryVM(category.getId(), category.getName(), category.getDescription(), category.isPublish(),
                createdAt,
                updatedAt
                , parentId);
    }
}
