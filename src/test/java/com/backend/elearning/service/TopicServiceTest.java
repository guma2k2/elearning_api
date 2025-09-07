package com.backend.elearning.service;

import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.category.CategoryRepository;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.topic.*;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.MessageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.StaticMessageSource;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CourseRepository courseRepository ;

    private TopicService topicService;

    @BeforeEach
    void beforeEach() {
        topicService = new TopicServiceImpl(topicRepository, categoryRepository, courseRepository);

        StaticMessageSource sms = new StaticMessageSource();
        sms.addMessage("TOPIC_NAME_DUPLICATED", Locale.ENGLISH, "Topic with name {0} is duplicated");

        sms.addMessage("TOPIC_NOT_FOUND", Locale.ENGLISH,
                "Topic {0} is not found");

        MessageUtil.setAccessor(new MessageSourceAccessor(sms, Locale.ENGLISH));
        LocaleContextHolder.setLocale(Locale.ENGLISH);
    }


    public TopicPostVM mockTopicPostVm () {
        String categoryName = "NonExistedCategory";

        return new TopicPostVM(null,"NewTopic", "Description", true, List.of(categoryName));
    }

    @Test
    void create_shouldCreateTopic_whenNoDuplicatesAndNoCategories() {
        // given
        TopicPostVM topicPostVM = new TopicPostVM(null,"NewTopic", "Description", true, List.of());
        when(topicRepository.countByNameAndId(topicPostVM.name(), null)).thenReturn(0L);

        // when
        TopicVM result = topicService.create(topicPostVM);

        // then
        verify(topicRepository, times(1)).saveAndFlush(any(Topic.class));
        verify(topicRepository, never()).save(any(Topic.class));

        assertEquals(topicPostVM.name(), result.name());
        assertEquals(topicPostVM.description(), result.description());
    }

    @Test
    void create_shouldCreateTopicAndAssociateCategories_whenNoDuplicatesAndCategoriesProvided() {
        // given
        String categoryName = "Category1";
        TopicPostVM topicPostVM = new TopicPostVM(null,"NewTopic", "Description", true, List.of(categoryName));
        when(topicRepository.countByNameAndId(topicPostVM.name(), null)).thenReturn(0L);

        Category category = new Category();
        category.setName(categoryName);
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(category));

        // when
        TopicVM result = topicService.create(topicPostVM);

        // then
        verify(topicRepository, times(1)).saveAndFlush(any(Topic.class));
        verify(topicRepository, times(1)).save(any(Topic.class));

        assertEquals(topicPostVM.name(), result.name());
        assertEquals(topicPostVM.description(), result.description());
    }

    @Test
    void create_shouldThrowDuplicateException_whenDuplicateTopicNameExists() {
        // given
        TopicPostVM topicPostVM = mockTopicPostVm();
        when(topicRepository.countByNameAndId(topicPostVM.name(), null)).thenReturn(1L);

        // when & then
        assertThrows(DuplicateException.class, () -> topicService.create(topicPostVM));

        verify(topicRepository, never()).saveAndFlush(any(Topic.class));
        verify(categoryRepository, never()).findByName(anyString());
    }

    @Test
    void create_shouldThrowNotFoundException_whenCategoryDoesNotExist() {
        // given
        String categoryName = "NonExistedCategory";
        TopicPostVM topicPostVM = mockTopicPostVm();
        when(topicRepository.countByNameAndId(topicPostVM.name(), null)).thenReturn(0L);
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> topicService.create(topicPostVM));

        verify(topicRepository, times(1)).saveAndFlush(any(Topic.class));
        verify(topicRepository, never()).save(any(Topic.class));
    }


    @Test
    void update_shouldUpdateTopic_whenNoDuplicateAndValidCategories() {
        // given
        Integer topicId = 1;
        TopicPostVM topicPostVM = new TopicPostVM(null,"UpdatedTopic", "UpdatedDescription", true, List.of("Category1", "Category2"));
        Topic existingTopic = new Topic();
        existingTopic.setId(topicId);

        when(topicRepository.countByNameAndId(topicPostVM.name(), topicId)).thenReturn(0L);
        when(topicRepository.findById(topicId)).thenReturn(Optional.of(existingTopic));
        when(categoryRepository.findAllByNameIn(topicPostVM.categories())).thenReturn(Set.of(new Category(), new Category()));

        // when
        topicService.update(topicPostVM, topicId);

        // then
        verify(topicRepository, times(1)).save(existingTopic);
        assertEquals(topicPostVM.name(), existingTopic.getName());
        assertEquals(topicPostVM.description(), existingTopic.getDescription());
        assertEquals(topicPostVM.isPublish(), existingTopic.isPublish());
        assertEquals(2, existingTopic.getCategories().size());
    }

    @Test
    void update_shouldThrowDuplicateException_whenDuplicateTopicNameExists() {
        // given
        Integer topicId = 1;
        TopicPostVM topicPostVM = new TopicPostVM(null,"ExistingTopic", "Description", true, List.of());

        when(topicRepository.countByNameAndId(topicPostVM.name(), topicId)).thenReturn(1L);

        // when & then
        assertThrows(DuplicateException.class, () -> topicService.update(topicPostVM, topicId));

        verify(topicRepository, never()).save(any(Topic.class));
    }

    @Test
    void update_shouldThrowNotFoundException_whenTopicDoesNotExist() {
        // given
        Integer topicId = 1;
        TopicPostVM topicPostVM = new TopicPostVM(null,"NewTopic", "Description", true, List.of());

        when(topicRepository.findById(topicId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> topicService.update(topicPostVM, topicId));

        verify(topicRepository, never()).save(any(Topic.class));
    }


    @Test
    void delete_shouldDeleteTopic_whenNoCategoriesOrCoursesExist() {
        // given
        Integer topicId = 1;
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setCategories(Set.of());

        when(topicRepository.findByIdReturnCategories(topicId)).thenReturn(Optional.of(topic));
        when(courseRepository.findByTopicId(topicId)).thenReturn(List.of());

        // when
        topicService.delete(topicId);

        // then
        verify(topicRepository, times(1)).delete(topic);
    }

    @Test
    void delete_shouldThrowBadRequestException_whenTopicHasCategories() {
        // given
        Integer topicId = 1;
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setCategories(Set.of(new Category()));

        when(topicRepository.findByIdReturnCategories(topicId)).thenReturn(Optional.of(topic));

        // when & then
        assertThrows(BadRequestException.class, () -> topicService.delete(topicId));

        verify(topicRepository, never()).delete(any(Topic.class));
    }

    @Test
    void delete_shouldThrowBadRequestException_whenTopicHasCourses() {
        // given
        Integer topicId = 1;
        Topic topic = new Topic();
        topic.setId(topicId);
        topic.setCategories(Set.of());

        when(topicRepository.findByIdReturnCategories(topicId)).thenReturn(Optional.of(topic));
        when(courseRepository.findByTopicId(topicId)).thenReturn(List.of(new Course()));

        // when & then
        assertThrows(BadRequestException.class, () -> topicService.delete(topicId));

        verify(topicRepository, never()).delete(any(Topic.class));
    }

    @Test
    void delete_shouldThrowNotFoundException_whenTopicDoesNotExist() {
        // given
        Integer topicId = 1;

        when(topicRepository.findByIdReturnCategories(topicId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> topicService.delete(topicId));

        verify(topicRepository, never()).delete(any(Topic.class));
    }

    @Test
    void getTopicsByCategoryId_shouldReturnListOfTopics_whenCategoryExists() {
        // given
        Integer categoryId = 1;
        Category category = new Category();
        category.setId(categoryId);

        Topic topic1 = new Topic();
        topic1.setId(1);
        Topic topic2 = new Topic();
        topic2.setId(2);

        category.setTopics(Set.of(topic1, topic2));

        when(categoryRepository.findByIdTopics(categoryId)).thenReturn(Optional.of(category));

        // when
        List<TopicVM> topics = topicService.getTopicsByCategoryId(categoryId);

        // then
        assertEquals(2, topics.size());
        verify(categoryRepository, times(1)).findByIdTopics(categoryId);
    }

    @Test
    void getTopicsByCategoryId_shouldThrowNotFoundException_whenCategoryDoesNotExist() {
        // given
        Integer categoryId = 1;

        when(categoryRepository.findByIdTopics(categoryId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> topicService.getTopicsByCategoryId(categoryId));

        verify(categoryRepository, times(1)).findByIdTopics(categoryId);
    }
}
