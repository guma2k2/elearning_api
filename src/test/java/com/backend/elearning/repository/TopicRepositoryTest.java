package com.backend.elearning.repository;

import com.backend.elearning.TestConfig;
import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.category.CategoryRepository;
import com.backend.elearning.domain.topic.Topic;
import com.backend.elearning.domain.topic.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TestConfig.class})
public class TopicRepositoryTest {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        // Clearing the repository before each test
        topicRepository.deleteAll();
        categoryRepository.deleteAll();

        // Setting up test data
        Category category1 = Category.builder()
                .name("Category 1")
                .description("Description 1")
                .publish(true)
                .build();

        Category category2 = Category.builder()
                .name("Category 2")
                .description("Description 2")
                .publish(true)
                .build();

        Set<Category> categories = new HashSet<>();
        categories.add(category1);
        categories.add(category2);

        Topic topic1 = Topic.builder()
                .name("Topic 1")
                .description("Description 1")
                .publish(true)
                .categories(categories)
                .build();

        Topic topic2 = Topic.builder()
                .name("Topic 2")
                .description("Description 2")
                .publish(false)
                .build();

        categoryRepository.saveAll(categories);
        topicRepository.saveAll(List.of(topic1, topic2));
    }

    @Test
    void testFindAllCustom_WithKeyword() {
        // Given
        String keyword = "Topic 1";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Topic> result = topicRepository.findAllCustom(pageable, keyword);

        // Then
        assertEquals(1, result.getTotalElements(), "Should return 1 topic that matches the keyword");
    }

    @Test
    void testFindAllCustom_WithEmptyKeyword() {
        // Given
        String keyword = "";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Topic> result = topicRepository.findAllCustom(pageable, keyword);

        // Then
        assertEquals(2, result.getTotalElements(), "Should return all topics when keyword is empty");
    }

    @Test
    void testCountByNameAndId_WithExistingNameAndDifferentId() {
        // Given
        String name = "Topic 1";
        Integer id = 999;  // ID that doesn't exist in the database

        // When
        Long count = topicRepository.countByNameAndId(name, id);

        // Then
        assertEquals(1L, count, "Should return 1 since name exists but ID does not match");
    }

    @Test
    void testCountByNameAndId_WithExistingNameAndSameId() {
        // Given
        Topic existingTopic = topicRepository.findAll().get(0);
        String name = existingTopic.getName();
        Integer id = existingTopic.getId();

        // When
        Long count = topicRepository.countByNameAndId(name, id);

        // Then
        assertEquals(0L, count, "Should return 0 since the name exists but the ID matches");
    }

    @Test
    void testFindByIdReturnCategories_WithExistingId() {
        // Given
        Topic topic = topicRepository.findAll().get(0);
        Integer id = topic.getId();

        // When
        Optional<Topic> result = topicRepository.findByIdReturnCategories(id);

        // Then
        assertTrue(result.isPresent(), "Topic should be found with the given ID");
        assertEquals("Topic 1", result.get().getName(), "The topic name should match");
        assertNotNull(result.get().getCategories(), "The topic should have categories");
        assertEquals(2, result.get().getCategories().size(), "The topic should have 2 categories");
    }

    @Test
    void testFindByIdReturnCategories_WithNonExistingId() {
        // Given
        Integer id = 999;  // Non-existing ID

        // When
        Optional<Topic> result = topicRepository.findByIdReturnCategories(id);

        // Then
        assertFalse(result.isPresent(), "No topic should be found with a non-existing ID");
    }
}
