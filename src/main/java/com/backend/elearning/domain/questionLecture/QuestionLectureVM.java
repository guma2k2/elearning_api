package com.backend.elearning.domain.questionLecture;

import com.backend.elearning.domain.lecture.LectureGetVM;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.user.UserGetVM;
import com.backend.elearning.utils.DateTimeUtils;

public record QuestionLectureVM (
        Long id,
        String title,
        String description,
        UserGetVM student,
        LectureGetVM lecture,
        String createdAt,
        String updatedAt
) {

    public static final QuestionLectureVM fromModel (QuestionLecture questionLecture) {
        Student student = questionLecture.getStudent();
        LectureGetVM lectureGetVM = LectureGetVM.fromModel(questionLecture.getLecture());
        UserGetVM userGetVM = UserGetVM.fromModelStudent(student);
        String createdAt = questionLecture.getCreatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(questionLecture.getCreatedAt()) : "";
        String updatedAt = questionLecture.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(questionLecture.getUpdatedAt()) : "";
        return new QuestionLectureVM(questionLecture.getId(),
                questionLecture.getTitle(),
                questionLecture.getDescription(),
                userGetVM,
                lectureGetVM,
                createdAt,
                updatedAt);
    }
}
