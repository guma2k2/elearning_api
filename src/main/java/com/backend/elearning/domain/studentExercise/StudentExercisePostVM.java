package com.backend.elearning.domain.studentExercise;

public record StudentExercisePostVM(
        Boolean submitted,
        Long exerciseId,
        String fileName,
        String fileUrl
) {
}
