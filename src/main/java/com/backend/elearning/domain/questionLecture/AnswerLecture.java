package com.backend.elearning.domain.questionLecture;

import com.backend.elearning.domain.questionLecture.studentAnswer.StudentAnswer;
import com.backend.elearning.domain.questionLecture.userAnswer.UserAnswer;
import com.backend.elearning.domain.questionLecture.userAnswer.UserAnswerVM;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserGetVM;
import com.backend.elearning.utils.DateTimeUtils;

public record AnswerLecture(
        Long id,
        String content,
        String createdAt,
        String updatedAt,
        UserGetVM user
) {

    public static final AnswerLecture fromModelStudent(StudentAnswer studentAnswer) {
        Student student = studentAnswer.getStudent();
        UserGetVM userGetVM = UserGetVM.fromModelStudent(student);
        String createdAt = studentAnswer.getCreatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(studentAnswer.getCreatedAt()) : "";
        String updatedAt = studentAnswer.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(studentAnswer.getUpdatedAt()) : "";
        return new AnswerLecture(studentAnswer.getId(), studentAnswer.getContent(),
                createdAt,
                updatedAt, userGetVM);
    }

    public static final AnswerLecture fromModelUser(UserAnswer userAnswer) {
        User user = userAnswer.getUser();
        UserGetVM userGetVM = UserGetVM.fromModel(user);
        String createdAt = userAnswer.getCreatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(userAnswer.getCreatedAt()) : "";
        String updatedAt = userAnswer.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(userAnswer.getUpdatedAt()) : "";
        return new AnswerLecture(userAnswer.getId(),
                userAnswer.getContent(),
                createdAt,
                updatedAt, userGetVM);
    }
}
