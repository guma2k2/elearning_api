package com.backend.elearning.domain.topic;

import com.backend.elearning.domain.category.Category;
import com.backend.elearning.utils.DateTimeUtils;

import java.util.List;
import java.util.Set;

public record TopicVM(
    Integer id,
    String name,
    String description,
    boolean isPublish,
    List<String> categories,
    String createdAt,
    String updatedAt
) {

    public static TopicVM fromModel(Topic topic){
        Set<Category> categories = topic.getCategories();
        List<String> categoriesName = categories.stream().map(Category::getName).toList();
        String createdAt = topic.getCreatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(topic.getCreatedAt()) : "";
        String updatedAt = topic.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(topic.getUpdatedAt()) : "";
        return new TopicVM(topic.getId(), topic.getName(),topic.getDescription() ,topic.isPublish(), categoriesName,createdAt, updatedAt);
    }
}
