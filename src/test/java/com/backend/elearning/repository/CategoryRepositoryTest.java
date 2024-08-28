package com.backend.elearning.repository;

import com.backend.elearning.TestConfig;
import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TestConfig.class})
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        // Clearing the repository before each test
        categoryRepository.deleteAll();

        // Setting up test data
        Category parentCategory = Category.builder()
                .name("Parent Category")
                .description("Parent Category Description")
                .publish(true)
                .build();

        Category childCategory1 = Category.builder()
                .name("Child Category 1")
                .description("Child Category 1 Description")
                .publish(true)
                .parent(parentCategory)
                .build();

        Category childCategory2 = Category.builder()
                .name("Child Category 2")
                .description("Child Category 2 Description")
                .publish(false)
                .parent(parentCategory)
                .build();

        parentCategory.setChildrenList(List.of(childCategory1, childCategory2));

        // Saving the parent category which will cascade to children
        categoryRepository.save(parentCategory);
    }

    @Test
    void testCountExistByName_WithExistingNameAndDifferentId() {
        // Given
        String name = "Parent Category";
        Integer id = 999;  // ID that doesn't exist in the database

        // When
        Long count = categoryRepository.countExistByName(name, id);

        // Then
        assertEquals(1L, count, "Should return 1 since name exists but ID does not match");
    }

    @Test
    void testCountExistByName_WithExistingNameAndSameId() {
        // Given
        Category existingCategory = categoryRepository.findAll().get(0);
        String name = existingCategory.getName();
        Integer id = existingCategory.getId();

        // When
        Long count = categoryRepository.countExistByName(name, id);

        // Then
        assertEquals(0L, count, "Should return 0 since the name exists but the ID matches");
    }

    @Test
    void testFindAllCustom_WithKeyword() {
        // Given
        String keyword = "Child Category 1";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Category> result = categoryRepository.findAllCustom(pageable, keyword);

        // Then
        assertEquals(1, result.getTotalElements(), "Should return 1 category that matches the keyword");
    }

    @Test
    void testFindAllCustom_WithEmptyKeyword() {
        // Given
        String keyword = "";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Category> result = categoryRepository.findAllCustom(pageable, keyword);

        // Then
        assertEquals(3, result.getTotalElements(), "Should return all categories when keyword is empty");
    }

    @Test
    void testFindByIdWithParent_WithExistingId() {
        // Given
        Category parentCategory = categoryRepository.findAll().get(0);
        Integer id = parentCategory.getId();

        // When
        Optional<Category> result = categoryRepository.findByIdWithParent(id);

        // Then
        assertTrue(result.isPresent(), "Category should be found with the given ID");
        assertEquals("Parent Category", result.get().getName(), "The category name should match");
        assertNotNull(result.get().getChildrenList(), "The category should have children");
        assertEquals(2, result.get().getChildrenList().size(), "The category should have 2 children");
    }

    @Test
    void testFindByIdWithParent_WithNonExistingId() {
        // Given
        Integer id = 999;  // Non-existing ID

        // When
        Optional<Category> result = categoryRepository.findByIdWithParent(id);

        // Then
        assertFalse(result.isPresent(), "No category should be found with a non-existing ID");
    }
}
