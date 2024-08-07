package com.backend.elearning.domain.category;

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
        String createdAt = category.getCreatedAt() != null ? category.getCreatedAt().toString() : "";
        String updatedAt = category.getUpdatedAt() != null ? category.getUpdatedAt().toString() : "";

        return new CategoryVM(category.getId(), category.getName(), category.getDescription(), category.isPublish(),
                createdAt,
                updatedAt
                , parentId);
    }
}
