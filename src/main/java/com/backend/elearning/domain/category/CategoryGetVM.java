package com.backend.elearning.domain.category;

import java.util.List;

public record CategoryGetVM(
        String name,
        int parentId,
        List<CategoryGetVM> childrens
) {
    public static CategoryGetVM fromModel(Category category, List<CategoryGetVM> childrens) {
        Category parent  = category.getParent();
        int parentId = parent != null ? parent.getId() : -1;
        return new CategoryGetVM(category.getName(), parentId, childrens);
    }

}
