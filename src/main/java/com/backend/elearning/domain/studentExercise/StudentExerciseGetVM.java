package com.backend.elearning.domain.studentExercise;

import com.backend.elearning.domain.user.UserGetVM;
import com.backend.elearning.utils.DateTimeUtils;

public record StudentExerciseGetVM(
        Long id,
        String fileName,
        String fileUrl,
        Boolean submitted,
        String submittedTime,
        UserGetVM student
) {
    public static StudentExerciseGetVM fromModel(StudentExercise studentExercise) {
        String submittedTime = studentExercise.getSubmittedTime() != null ?
                DateTimeUtils.convertLocalDateTimeToString(studentExercise.getSubmittedTime()) : "";
        UserGetVM student = UserGetVM.fromModelStudent(studentExercise.getStudent());
        return new StudentExerciseGetVM(studentExercise.getId(), studentExercise.getFileName(), studentExercise.getFileUrl(),
                studentExercise.getSubmitted(), submittedTime, student);
    }
}
