package com.backend.elearning.domain.questionLecture.studentAnswer;

import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.user.UserGetVM;

public record StudentAnswerVM(
        Long id,
        String content,
        String createdAt,
        String updatedAt,
        UserGetVM student
) {
    public static final StudentAnswerVM fromModel(StudentAnswer studentAnswer) {
        Student student = studentAnswer.getStudent();
        UserGetVM userGetVM = UserGetVM.fromModelStudent(student);
        return new StudentAnswerVM(studentAnswer.getId(), studentAnswer.getContent(), studentAnswer.getCreatedAt().toString(),
                studentAnswer.getUpdatedAt().toString(), userGetVM);
    }
}
