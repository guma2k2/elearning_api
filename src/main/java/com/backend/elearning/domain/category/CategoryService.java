package com.backend.elearning.domain.category;

import com.backend.elearning.domain.common.PageableData;

import java.util.List;

public interface CategoryService {

    PageableData<CategoryVM> getPageableCategories(int pageNum, int pageSize, String keyword);
    CategoryVM create (CategoryPostVM categoryPostVM);
    CategoryVM getCategoryById (Integer categoryId);
    List<CategoryListGetVM> getCategoryParents();
    void update(CategoryPostVM categoryPutVM, Integer categoryId);
    void delete(Integer categoryId);

    CategoryGetVM getByName(String name);
}
