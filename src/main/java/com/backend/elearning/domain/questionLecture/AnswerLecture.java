package com.backend.elearning.domain.questionLecture;

import com.backend.elearning.domain.questionLecture.studentAnswer.StudentAnswer;
import com.backend.elearning.domain.questionLecture.userAnswer.UserAnswer;
import com.backend.elearning.domain.questionLecture.userAnswer.UserAnswerVM;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserGetVM;

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
        return new AnswerLecture(studentAnswer.getId(), studentAnswer.getContent(), studentAnswer.getCreatedAt().toString(),
                studentAnswer.getUpdatedAt().toString(), userGetVM);
    }

    public static final AnswerLecture fromModelUser(UserAnswer userAnswer) {
        User user = userAnswer.getUser();
        UserGetVM userGetVM = UserGetVM.fromModel(user);
        return new AnswerLecture(userAnswer.getId(), userAnswer.getContent(), userAnswer.getCreatedAt().toString(),
                userAnswer.getUpdatedAt().toString(), userGetVM);
    }
}
