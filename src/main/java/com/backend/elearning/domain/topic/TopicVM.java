package com.backend.elearning.domain.topic;

import com.backend.elearning.domain.category.Category;

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
        String createdAt = topic.getCreatedAt() != null ? topic.getCreatedAt().toString() : "";
        String updatedAt = topic.getUpdatedAt() != null ? topic.getUpdatedAt().toString() : "";
        return new TopicVM(topic.getId(), topic.getName(),topic.getDescription() ,topic.isPublish(), categoriesName,createdAt, updatedAt);
    }
}
