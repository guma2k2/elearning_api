package com.backend.elearning.domain.questionLecture;

import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.user.UserGetVM;

public record QuestionLectureVM (
        Long id,
        String title,
        String description,

        UserGetVM student,
        String createdAt,
        String updatedAt
) {

    public static final QuestionLectureVM fromModel (QuestionLecture questionLecture) {
        Student student = questionLecture.getStudent();
        UserGetVM userGetVM = UserGetVM.fromModelStudent(student);
        return new QuestionLectureVM(questionLecture.getId(),
                questionLecture.getTitle(),
                questionLecture.getDescription(),
                userGetVM,
                questionLecture.getCreatedAt().toString(),
                questionLecture.getUpdatedAt().toString());
    }
}
