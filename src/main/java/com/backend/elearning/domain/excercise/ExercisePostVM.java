package com.backend.elearning.domain.excercise;

public record ExercisePostVM(
        String title,
        String description,
        String deadline,
        Long classroomId
) {
}
