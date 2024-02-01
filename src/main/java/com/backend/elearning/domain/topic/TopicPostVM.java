package com.backend.elearning.domain.topic;

import java.util.List;

public record TopicPostVM(Integer id, String name, boolean isPublish, List<String> categories) {
}
