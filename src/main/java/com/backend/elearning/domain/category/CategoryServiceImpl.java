package com.backend.elearning.domain.category;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.topic.TopicRepository;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private static final String SORT_BY = "updatedAt";
    private final CourseRepository courseRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository, CourseRepository courseRepository) {
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public PageableData<CategoryVM> getPageableCategories(int pageNum, int pageSize, String keyword) {
        log.info("received pageNum: {}, pageSize: {}, keyword: {}", pageNum, pageSize, keyword);
        List<CategoryVM> categoryGetVms = new ArrayList<>();
        Sort sort = Sort.by(SORT_BY);
        sort.descending();
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Category> categoryPage = categoryRepository.findAllCustom(pageable, keyword);
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
    public CategoryVM create(CategoryPostVM categoryPostVM) {
        log.info("received request: {}", categoryPostVM);
        if (categoryRepository.countExistByName(categoryPostVM.name(), null) > 0L) {
            throw new DuplicateException(Constants.ERROR_CODE.CATEGORY_NAME_DUPLICATED, categoryPostVM.name());
        }
        Category category = Category.builder()
                .name(categoryPostVM.name())
                .description(categoryPostVM.description())
                .publish(categoryPostVM.isPublish())
                .build();
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        if (categoryPostVM.parentId() != null) {
            Category parent = categoryRepository.findById(categoryPostVM.parentId()).orElseThrow(() ->
                    new NotFoundException(Constants.ERROR_CODE.CATEGORY_NOT_FOUND, categoryPostVM.parentId()));
            category.setParent(parent);
        }
        Category savedCategory = categoryRepository.saveAndFlush(category);
        return CategoryVM.fromModel(savedCategory);
    }
    @Override
    public CategoryVM getCategoryById(Integer categoryId) {
        log.info("received categoryId: {}", categoryId);
        Category category = categoryRepository.findByIdWithParent(categoryId).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.CATEGORY_NOT_FOUND, categoryId));
        CategoryVM categoryVM = CategoryVM.fromModel(category);
        return categoryVM;
    }

    @Override
    public List<CategoryListGetVM> getCategoryParents() {
        List<Category> categoryParentList = categoryRepository.findAllParents();
        List<CategoryListGetVM> categoryListGetVMs = categoryParentList.stream()
                .map(CategoryListGetVM::fromModel)
                .toList();
        return categoryListGetVMs;
    }

    @Override
    public void update(CategoryPostVM categoryPutVM, Integer categoryId) {
        log.info("received categoryPutVM: {}", categoryPutVM);
        if (categoryRepository.countExistByName(categoryPutVM.name(), categoryId) > 0l) {
            throw new DuplicateException(Constants.ERROR_CODE.CATEGORY_NAME_DUPLICATED, categoryPutVM.name());
        }
        Category category = categoryRepository.findByIdWithParent(categoryId).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.CATEGORY_NOT_FOUND,categoryId));
        category.setName(categoryPutVM.name());
        category.setDescription(categoryPutVM.description());
        category.setPublish(categoryPutVM.isPublish());
        category.setUpdatedAt(LocalDateTime.now());
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
        log.info("received categoryId: {}", categoryId);
        Category category = categoryRepository.findByIdWithParent(categoryId).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.CATEGORY_NOT_FOUND, categoryId));
        if (category.getChildrenList().size() > 0) {
            throw new BadRequestException("Category had children");
        }
        List<Course> courses = courseRepository.findByCategoryId(categoryId);
        if (courses.size() > 0) {
            throw new BadRequestException("Category had course");
        }
        Category categoryWithTopic = categoryRepository.findByIdTopics(categoryId).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.CATEGORY_NOT_FOUND, categoryId));
        if (categoryWithTopic.getTopics().size() > 0) {
            throw new BadRequestException("Category had topic");
        }
        categoryRepository.delete(category);
    }

    @Override
    public CategoryGetVM getByName(String name) {
        log.info("received categoryName: {}", name);
        Category category = categoryRepository.findByNameCustom(name).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.CATEGORY_NOT_FOUND, name)); ;
        List<Category> childrenList = category.getChildrenList();
        if (childrenList.size() > 0) {
            List<CategoryGetVM> childVMs = childrenList
                    .stream()
                    .map(cat -> CategoryGetVM.fromModel(cat, new ArrayList<>()))
                    .toList() ;
            return CategoryGetVM.fromModel(category,childVMs);
        }
        return CategoryGetVM.fromModel(category, new ArrayList<>());
    }


    public boolean checkParent (Integer id, Category category) {
        if (id.equals(category.getId())) {
            return false;
        }
        if (category.getParent() != null) {
            return checkParent(id, category.getParent());
        }
        return true;
    }

}


