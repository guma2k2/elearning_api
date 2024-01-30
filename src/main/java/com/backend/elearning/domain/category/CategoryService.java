package com.backend.elearning.domain.category;

import com.backend.elearning.domain.common.PageableData;

public interface CategoryService {

    PageableData<CategoryVM> getPageableCategories(int pageNum, int pageSize);
    void create (CategoryPostVM categoryPostVM);
    void getCategoryById (Integer categoryId);
    void update(CategoryPostVM categoryPutVM, Integer categoryId);
    void delete(Integer categoryId);
}
