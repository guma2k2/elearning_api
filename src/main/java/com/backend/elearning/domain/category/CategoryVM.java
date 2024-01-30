package com.backend.elearning.domain.category;

public record CategoryVM(int id,
                         String name,
                         String description,
                         int parentId
) {
    public static CategoryVM fromModel(Category category) {
        Category parent  = category.getParent();
        int parentId = parent != null ? parent.getId() : -1;
        return new CategoryVM(category.getId(), category.getName(), category.getDescription(), parentId);
    }
}
