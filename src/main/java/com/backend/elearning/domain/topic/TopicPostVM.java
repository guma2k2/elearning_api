package com.backend.elearning.domain.topic;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record TopicPostVM(Integer id,
                          @NotBlank(message = "name must not be blank")
                          String name,
                          String description,
                          boolean isPublish,
                          List<String> categories
) {
}
