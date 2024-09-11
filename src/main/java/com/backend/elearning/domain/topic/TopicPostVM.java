package com.backend.elearning.domain.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record TopicPostVM(Integer id,
                          @NotEmpty(message = "name must not be empty")
                          String name,
                          String description,
                          boolean isPublish,
                          List<String> categories
) {
}
