package com.backend.elearning.domain.excercise;

import com.backend.elearning.utils.DateTimeUtils;

public record ExerciseVM(
    Long id,
    String title,
    String deadline,
    String description
) {
    public static ExerciseVM fromModel(Exercise exercise) {
        String deadline = exercise.getSubmission_deadline() != null ?
                DateTimeUtils.convertLocalDateTimeToString(exercise.getSubmission_deadline()) : "";
        return new ExerciseVM(exercise.getId(), exercise.getTitle(), deadline, exercise.getDescription());
    }
}
