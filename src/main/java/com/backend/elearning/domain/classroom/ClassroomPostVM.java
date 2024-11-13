package com.backend.elearning.domain.classroom;

public record ClassroomPostVM(
        String name,
        String description,
        String image,
        Long courseId
) {
}
