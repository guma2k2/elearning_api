package com.backend.elearning.domain.exerciseFile;

public record ExerciseFilePostVM(
        String fileName,
        String fileUrl,
        Long exerciseId
) {
}
