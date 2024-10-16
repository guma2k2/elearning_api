package com.backend.elearning.domain.course;

public record CourseStatusPostVM(
        CourseStatus status,
        String reason
) {
}
