package com.backend.elearning.domain.category;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PageableData<CategoryVM> getPageableCategories(int pageNum, int pageSize) {
        List<CategoryVM> categoryGetVms = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNum, pageSize);


        Page<Category> categoryPage = categoryRepository.findAllWithParent(pageable);
        List<Category> categories = categoryPage.getContent();
        for (Category category : categories) {
            categoryGetVms.add(CategoryVM.fromModel(category));
        }

        return new PageableData(
                pageNum,
                pageSize,
                (int) categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryGetVms
        );
    }

    @Override
    public void create(CategoryPostVM categoryPostVM) {
        if (categoryRepository.countExistByName(categoryPostVM.name(), null) > 0l) {
            throw new DuplicateException(Constants.ERROR_CODE.CATEGORY_NOT_FOUND);
        }
        Category category = Category.builder()
                .name(categoryPostVM.name())
                .description(categoryPostVM.description())
                .build();
        if (categoryPostVM.parentId() != null) {
            Category parent = categoryRepository.findById(categoryPostVM.parentId()).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.CATEGORY_NOT_FOUND, categoryPostVM.parentId()));
            category.setParent(parent);
        }

        categoryRepository.saveAndFlush(category);
    }
    @Override
    public void getCategoryById(Integer categoryId) {

    }

    @Override
    public void update(CategoryPostVM categoryPutVM, Integer categoryId) {
        if (categoryRepository.countExistByName(categoryPutVM.name(), categoryId) > 0l) {
            throw new DuplicateException(Constants.ERROR_CODE.CATEGORY_NOT_FOUND);
        }
        Category category = categoryRepository.findByIdWithParent(categoryId).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.CATEGORY_NOT_FOUND,categoryId));
        category.setName(categoryPutVM.name());
        category.setDescription(categoryPutVM.description());
        if (categoryPutVM.parentId() == null) {
            category.setParent(null);
        } else {
            Category parentCategory = categoryRepository
                    .findById(categoryPutVM.parentId())
                    .orElseThrow(() -> new BadRequestException(Constants.ERROR_CODE.PARENT_CATEGORY_NOT_FOUND, categoryPutVM.parentId()));

            if(!checkParent(category.getId(), parentCategory)){
                throw new BadRequestException(Constants.ERROR_CODE.PARENT_CATEGORY_CANNOT_BE_ITSELF);
            }
            category.setParent(parentCategory);
        }
        categoryRepository.saveAndFlush(category);
    }

    @Override
    public void delete(Integer categoryId) {

    }


    private boolean checkParent (Integer id, Category category) {
        if (id.equals(category.getId())) {
            return false;
        }
        if (category.getParent() != null) {
            return checkParent(id, category.getParent());
        }
        return true;
    }

}


