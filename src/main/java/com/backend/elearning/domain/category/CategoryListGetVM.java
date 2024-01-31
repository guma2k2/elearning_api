package com.backend.elearning.domain.category;

import java.util.List;

public record CategoryListGetVM(int id,
                                String name,
                                String description,
                                boolean isPublish,
                                List<CategoryVM> childrens
) {
    public static CategoryListGetVM fromModel(Category category) {
        List<CategoryVM> categoryChildrenList = category.getChildrenList()
                .stream().map(CategoryVM::fromModel).toList();
        return new CategoryListGetVM(category.getId(), category.getName(), category.getDescription(), category.isPublish(), categoryChildrenList);
    }
}
