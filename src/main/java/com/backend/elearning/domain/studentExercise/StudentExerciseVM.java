package com.backend.elearning.domain.studentExercise;

import com.backend.elearning.utils.DateTimeUtils;

public record StudentExerciseVM(
        Long id,
        String fileName,
        String fileUrl,
        Boolean submitted,
        String submittedTime
) {
    public static StudentExerciseVM fromModel(StudentExercise studentExercise) {
        String submittedTime = studentExercise.getSubmittedTime() != null ?
                DateTimeUtils.convertLocalDateTimeToString(studentExercise.getSubmittedTime()) : "";
        return new StudentExerciseVM(studentExercise.getId(), studentExercise.getFileName(), studentExercise.getFileUrl(),
                studentExercise.getSubmitted(), submittedTime);
    }
}
