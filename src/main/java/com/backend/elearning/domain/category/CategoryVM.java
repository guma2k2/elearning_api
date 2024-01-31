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
        return new CategoryVM(category.getId(), category.getName(), category.getDescription(), category.isPublish(),
                category.getCreatedAt().toString(),
                category.getUpdatedAt().toString()
                , parentId);
    }
}
