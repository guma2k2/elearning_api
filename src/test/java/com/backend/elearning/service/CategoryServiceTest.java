package com.backend.elearning.service;

import com.backend.elearning.domain.category.*;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.topic.Topic;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CourseRepository courseRepository;
    private CategoryServiceImpl categoryService;
    @BeforeEach
    void beforeEach() {
        categoryService = new CategoryServiceImpl(categoryRepository, courseRepository);
    }

    @Test
    void create_shouldCreateCategory_whenValidInputAndNoDuplicate() {
        // Given
        CategoryPostVM categoryPostVM = new CategoryPostVM(null,"CategoryName", "Category Description", true, null);
        Category category = Category.builder()
                .id(1)
                .name(categoryPostVM.name())
                .description(categoryPostVM.description())
                .publish(categoryPostVM.isPublish())
                .parent(null)
                .build();
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        when(categoryRepository.countExistByName(categoryPostVM.name(), null)).thenReturn(0L);
        when(categoryRepository.saveAndFlush(Mockito.any(Category.class))).thenReturn(category);
        System.out.println(category.getId());
        // When
        CategoryVM result = categoryService.create(categoryPostVM);

        // Then
        assertEquals(categoryPostVM.name(), result.name());
        assertEquals(categoryPostVM.description(), result.description());
        assertEquals(categoryPostVM.isPublish(), result.isPublish());

        verify(categoryRepository, times(1)).countExistByName(categoryPostVM.name(), null);
        verify(categoryRepository, times(1)).saveAndFlush(any(Category.class));
    }

    @Test
    void create_shouldThrowDuplicateException_whenCategoryNameExists() {
        // Given
        CategoryPostVM categoryPostVM = new CategoryPostVM(null, "CategoryName", "Category Description", true, null);

        when(categoryRepository.countExistByName(categoryPostVM.name(), null)).thenReturn(1L);

        // When
        assertThrows(DuplicateException.class, () -> categoryService.create(categoryPostVM));


        // Then
        verify(categoryRepository, times(1)).countExistByName(categoryPostVM.name(), null);
        verify(categoryRepository, never()).saveAndFlush(any(Category.class));
    }

    @Test
    void create_shouldCreateCategoryWithParent_whenParentIdIsProvided() {
        // Given
        Integer parentId = 1;
        CategoryPostVM categoryPostVM = new CategoryPostVM(null,"CategoryName", "Category Description", true, parentId);
        Category parentCategory = new Category();
        parentCategory.setId(parentId);
        Category category = Category.builder()
                .id(2)
                .name(categoryPostVM.name())
                .description(categoryPostVM.description())
                .publish(categoryPostVM.isPublish())
                .parent(parentCategory)
                .build();
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        when(categoryRepository.countExistByName(categoryPostVM.name(), null)).thenReturn(0L);
        when(categoryRepository.findById(parentId)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.saveAndFlush(any(Category.class))).thenReturn(category);

        // When
        CategoryVM result = categoryService.create(categoryPostVM);

        // Then
        assertEquals(categoryPostVM.name(), result.name());
        assertEquals(categoryPostVM.description(), result.description());
        assertEquals(categoryPostVM.isPublish(), result.isPublish());
        assertEquals(parentCategory.getId(), result.parentId());

        verify(categoryRepository, times(1)).countExistByName(categoryPostVM.name(), null);
        verify(categoryRepository, times(1)).findById(parentId);
        verify(categoryRepository, times(1)).saveAndFlush(any(Category.class));
    }

    @Test
    void create_shouldThrowNotFoundException_whenParentCategoryDoesNotExist() {
        // Given
        Integer parentId = 1;
        CategoryPostVM categoryPostVM = new CategoryPostVM(null,"CategoryName", "Category Description", true, parentId);

        when(categoryRepository.countExistByName(categoryPostVM.name(), null)).thenReturn(0L);
        when(categoryRepository.findById(parentId)).thenReturn(Optional.empty());

        // When and then
        assertThrows(NotFoundException.class, () -> categoryService.create(categoryPostVM));

        verify(categoryRepository, times(1)).countExistByName(categoryPostVM.name(), null);
        verify(categoryRepository, times(1)).findById(parentId);
        verify(categoryRepository, never()).saveAndFlush(any(Category.class));
    }

    @Test
    void update_shouldUpdateCategory_whenValidInputAndNoDuplicate() {
        // given
        Integer categoryId = 1;
        CategoryPostVM categoryPutVM = new CategoryPostVM(categoryId, "UpdatedName", "Updated Description", true, null);
        Category existingCategory = new Category();
        existingCategory.setId(categoryId);

        when(categoryRepository.countExistByName(categoryPutVM.name(), categoryId)).thenReturn(0L);
        when(categoryRepository.findByIdWithParent(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.saveAndFlush(any(Category.class))).thenReturn(existingCategory);

        // when
        categoryService.update(categoryPutVM, categoryId);

        // then
        verify(categoryRepository, times(1)).countExistByName(categoryPutVM.name(), categoryId);
        verify(categoryRepository, times(1)).findByIdWithParent(categoryId);
        verify(categoryRepository, times(1)).saveAndFlush(existingCategory);

        assertEquals(categoryPutVM.name(), existingCategory.getName());
        assertEquals(categoryPutVM.description(), existingCategory.getDescription());
        assertEquals(categoryPutVM.isPublish(), existingCategory.isPublish());
    }

    @Test
    void update_shouldThrowDuplicateException_whenCategoryNameExists() {
        // given
        Integer categoryId = 1;
        CategoryPostVM categoryPutVM = new CategoryPostVM(categoryId,"ExistingName", "Updated Description", true, null);

        when(categoryRepository.countExistByName(categoryPutVM.name(), categoryId)).thenReturn(1L);

        // when & then
        assertThrows(DuplicateException.class, () -> categoryService.update(categoryPutVM, categoryId));

        verify(categoryRepository, times(1)).countExistByName(categoryPutVM.name(), categoryId);
        verify(categoryRepository, never()).findByIdWithParent(anyInt());
        verify(categoryRepository, never()).saveAndFlush(any(Category.class));
    }

    @Test
    void update_shouldThrowNotFoundException_whenCategoryDoesNotExist() {
        // given
        Integer categoryId = 1;
        CategoryPostVM categoryPutVM = new CategoryPostVM(categoryId, "UpdatedName", "Updated Description", true, null);

        when(categoryRepository.findByIdWithParent(categoryId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> categoryService.update(categoryPutVM, categoryId));

        verify(categoryRepository, times(1)).findByIdWithParent(categoryId);
        verify(categoryRepository, never()).saveAndFlush(any(Category.class));
    }

    @Test
    void update_shouldThrowBadRequestException_whenParentCategoryDoesNotExist() {
        // given
        Integer categoryId = 1;
        Integer parentId = 2;
        CategoryPostVM categoryPutVM = new CategoryPostVM(categoryId, "UpdatedName", "Updated Description", true, parentId);
        Category existingCategory = new Category();
        existingCategory.setId(categoryId);

        when(categoryRepository.countExistByName(categoryPutVM.name(), categoryId)).thenReturn(0L);
        when(categoryRepository.findByIdWithParent(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.findById(parentId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(BadRequestException.class, () -> categoryService.update(categoryPutVM, categoryId));

        verify(categoryRepository, times(1)).findByIdWithParent(categoryId);
        verify(categoryRepository, times(1)).findById(parentId);
        verify(categoryRepository, never()).saveAndFlush(any(Category.class));
    }



    @Test
    void update_shouldUpdateCategoryWithParent_whenParentCategoryIsValid() {
        // given
        Integer categoryId = 1;
        Integer parentId = 2;
        CategoryPostVM categoryPutVM = new CategoryPostVM(categoryId, "UpdatedName", "Updated Description", true, parentId);
        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        Category parentCategory = new Category();
        parentCategory.setId(parentId);

        when(categoryRepository.countExistByName(categoryPutVM.name(), categoryId)).thenReturn(0L);
        when(categoryRepository.findByIdWithParent(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.findById(parentId)).thenReturn(Optional.of(parentCategory));

        // when
        categoryService.update(categoryPutVM, categoryId);

        // then
        verify(categoryRepository, times(1)).findByIdWithParent(categoryId);
        verify(categoryRepository, times(1)).findById(parentId);
        verify(categoryRepository, times(1)).saveAndFlush(existingCategory);

        assertEquals(categoryPutVM.name(), existingCategory.getName());
        assertEquals(categoryPutVM.description(), existingCategory.getDescription());
        assertEquals(categoryPutVM.isPublish(), existingCategory.isPublish());
        assertEquals(parentCategory, existingCategory.getParent());
    }

    @Test
    void delete_shouldDeleteCategory_whenNoChildrenCoursesOrTopics() {
        // given
        Integer categoryId = 1;
        Category category = new Category();
        category.setId(categoryId);
        category.setChildrenList(Collections.emptyList());
        category.setTopics(Collections.emptySet());

        when(categoryRepository.findByIdWithParent(categoryId)).thenReturn(Optional.of(category));
        when(courseRepository.findByCategoryId(categoryId)).thenReturn(Collections.emptyList());
        when(categoryRepository.findByIdTopics(categoryId)).thenReturn(Optional.of(category));

        // when
        categoryService.delete(categoryId);

        // then
        verify(categoryRepository, times(1)).findByIdWithParent(categoryId);
        verify(courseRepository, times(1)).findByCategoryId(categoryId);
        verify(categoryRepository, times(1)).findByIdTopics(categoryId);
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void delete_shouldThrowNotFoundException_whenCategoryDoesNotExist() {
        // given
        Integer categoryId = 1;

        when(categoryRepository.findByIdWithParent(categoryId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> categoryService.delete(categoryId));

        verify(categoryRepository, times(1)).findByIdWithParent(categoryId);
        verify(courseRepository, never()).findByCategoryId(anyInt());
        verify(categoryRepository, never()).findByIdTopics(anyInt());
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    void delete_shouldThrowBadRequestException_whenCategoryHasChildren() {
        // given
        Integer categoryId = 1;
        Category category = new Category();
        category.setId(categoryId);
        category.setChildrenList(List.of(new Category())); // Simulating a child category

        when(categoryRepository.findByIdWithParent(categoryId)).thenReturn(Optional.of(category));

        // when & then
        assertThrows(BadRequestException.class, () -> categoryService.delete(categoryId));

        verify(categoryRepository, times(1)).findByIdWithParent(categoryId);
        verify(courseRepository, never()).findByCategoryId(anyInt());
        verify(categoryRepository, never()).findByIdTopics(anyInt());
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    void delete_shouldThrowBadRequestException_whenCategoryHasCourses() {
        // given
        Integer categoryId = 1;
        Category category = new Category();
        category.setId(categoryId);
        category.setChildrenList(Collections.emptyList());

        when(categoryRepository.findByIdWithParent(categoryId)).thenReturn(Optional.of(category));
        when(courseRepository.findByCategoryId(categoryId)).thenReturn(List.of(new Course())); // Simulating a course

        // when & then
        assertThrows(BadRequestException.class, () -> categoryService.delete(categoryId));

        verify(categoryRepository, times(1)).findByIdWithParent(categoryId);
        verify(courseRepository, times(1)).findByCategoryId(categoryId);
        verify(categoryRepository, never()).findByIdTopics(anyInt());
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    void delete_shouldThrowBadRequestException_whenCategoryHasTopics() {
        // given
        Integer categoryId = 1;
        Category category = new Category();
        category.setId(categoryId);
        category.setChildrenList(Collections.emptyList());
        category.setTopics(Set.of(new Topic())); // Simulating a topic

        when(categoryRepository.findByIdWithParent(categoryId)).thenReturn(Optional.of(category));
        when(courseRepository.findByCategoryId(categoryId)).thenReturn(Collections.emptyList());
        when(categoryRepository.findByIdTopics(categoryId)).thenReturn(Optional.of(category));

        // when & then
        assertThrows(BadRequestException.class, () -> categoryService.delete(categoryId));

        verify(categoryRepository, times(1)).findByIdWithParent(categoryId);
        verify(courseRepository, times(1)).findByCategoryId(categoryId);
        verify(categoryRepository, times(1)).findByIdTopics(categoryId);
        verify(categoryRepository, never()).delete(any(Category.class));
    }
    @Test
    void getByName_shouldReturnCategoryGetVM_whenCategoryExistsWithoutChildren() {
        // given
        String categoryName = "TestCategory";
        Category category = new Category();
        category.setName(categoryName);
        category.setChildrenList(new ArrayList<>());

        when(categoryRepository.findByNameCustom(categoryName)).thenReturn(Optional.of(category));

        // when
        CategoryGetVM result = categoryService.getByName(categoryName);

        // then
        verify(categoryRepository, times(1)).findByNameCustom(categoryName);
        assertEquals(categoryName, result.name());
        assertEquals(0, result.childrens().size());
    }

    @Test
    void getByName_shouldReturnCategoryGetVMWithChildren_whenCategoryExistsWithChildren() {
        // given
        String categoryName = "ParentCategory";
        Category childCategory = new Category();
        childCategory.setName("ChildCategory");
        Category category = new Category();
        category.setName(categoryName);
        category.setChildrenList(List.of(childCategory));

        when(categoryRepository.findByNameCustom(categoryName)).thenReturn(Optional.of(category));

        // when
        CategoryGetVM result = categoryService.getByName(categoryName);

        // then
        verify(categoryRepository, times(1)).findByNameCustom(categoryName);
        assertEquals(categoryName, result.name());
        assertEquals(1, result.childrens().size());
        assertEquals("ChildCategory", result.childrens().get(0).name());
    }

    @Test
    void getByName_shouldThrowNotFoundException_whenCategoryDoesNotExist() {
        // given
        String categoryName = "NonExistentCategory";

        when(categoryRepository.findByNameCustom(categoryName)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> categoryService.getByName(categoryName));

        verify(categoryRepository, times(1)).findByNameCustom(categoryName);
    }
}
