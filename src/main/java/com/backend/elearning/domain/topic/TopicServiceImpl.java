package com.backend.elearning.domain.topic;

import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.category.CategoryRepository;
import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class TopicServiceImpl implements TopicService{

    private final TopicRepository topicRepository;

    private final CategoryRepository categoryRepository;

    private final CourseRepository courseRepository ;
    private final String sortBy = "updatedAt";

    public TopicServiceImpl(TopicRepository topicRepository, CategoryRepository categoryRepository, CourseRepository courseRepository) {
        this.topicRepository = topicRepository;
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public PageableData<TopicVM> getPageableTopics(int pageNum, int pageSize, String keyword) {
        List<TopicVM> topicVMS = new ArrayList<>();
        Sort sort = Sort.by(sortBy);
        sort.descending();
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Topic> topicPage = topicRepository.findAllCustom(pageable, keyword);
        List<Topic> topics = topicPage.getContent();
        for (Topic topic : topics) {
            topicVMS.add(TopicVM.fromModel(topic));
        }
        return new PageableData(
                pageNum,
                pageSize,
                (int) topicPage.getTotalElements(),
                topicPage.getTotalPages(),
                topicVMS
        );
    }

    @Override
    public TopicVM create(TopicPostVM topicPostVM) {
        if (topicRepository.countByNameAndId(topicPostVM.name(), null) > 0){
            throw new DuplicateException(Constants.ERROR_CODE.TOPIC_NAME_DUPLICATED, topicPostVM.name());
        }
        Topic topic = Topic.builder()
                .name(topicPostVM.name())
                .description(topicPostVM.description())
                .publish(topicPostVM.isPublish())
                .build();
        topic.setCreatedAt(LocalDateTime.now());
        topic.setUpdatedAt(LocalDateTime.now());
        topicRepository.saveAndFlush(topic);
        if (!topicPostVM.categories().isEmpty()) {
            for (String catName: topicPostVM.categories()) {
                Category category = categoryRepository.findByName(catName).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.CATEGORY_NOT_FOUND));
                topic.addCategory(category);
            }
            topicRepository.save(topic);
        }
        return TopicVM.fromModel(topic);
    }

    @Override
    public TopicVM getTopicById(Integer topicId) {
        Topic topic = topicRepository.findByIdReturnCategories(topicId).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.TOPIC_NOT_FOUND));
        return TopicVM.fromModel(topic);
    }

    @Override
    public List<TopicVM> getTopicsByCategoryId(Integer categoryId) {
        Category category = categoryRepository.findByIdTopics(categoryId).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.CATEGORY_NOT_FOUND, categoryId));
        List<TopicVM> topics = category.getTopics().stream().map(TopicVM::fromModel).toList();
        return topics;
    }

    @Override
    public void update(TopicPostVM topicPostVM, Integer topicId) {
        if (topicRepository.countByNameAndId(topicPostVM.name(), topicId) > 0){
            throw new DuplicateException(Constants.ERROR_CODE.TOPIC_NAME_DUPLICATED, topicPostVM.name());
        }
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.TOPIC_NOT_FOUND));
        topic.setName(topicPostVM.name());
        topic.setPublish(topicPostVM.isPublish());
        topic.setDescription(topicPostVM.description());
        topic.setUpdatedAt(LocalDateTime.now());
        Set<Category> newCategories = new HashSet<>(categoryRepository.findAllByNameIn(topicPostVM.categories()));
        topic.setCategories(newCategories);
        log.info(String.valueOf(newCategories.size()));
        topicRepository.save(topic);
    }

    @Override
    public void delete(Integer topicId) {
        Topic topic = topicRepository.findByIdReturnCategories(topicId).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.TOPIC_NOT_FOUND, topicId));
        if (topic.getCategories().size() > 0) {
            throw new BadRequestException("Topic had category");
        }
        List<Course> courses = courseRepository.findByTopicId(topicId);
        if (courses.size() > 0) {
            throw new BadRequestException("Topic had course");
        }
        topicRepository.delete(topic);
    }
}
