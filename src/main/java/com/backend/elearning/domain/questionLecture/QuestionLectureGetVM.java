package com.backend.elearning.domain.questionLecture;

import com.backend.elearning.domain.lecture.LectureGetVM;
import com.backend.elearning.domain.user.UserGetVM;

import java.util.List;

public record QuestionLectureGetVM(
        Long id,
        String title,
        String description,
        UserGetVM student,
        LectureGetVM lecture,
        String createdAt,
        String updatedAt,
        List<AnswerLecture> answers
) {
    public static QuestionLectureGetVM fromModel(QuestionLecture questionLecture, List<AnswerLecture> answers) {
        LectureGetVM lectureGetVM = LectureGetVM.fromModel(questionLecture.getLecture());
        UserGetVM userGetVM = UserGetVM.fromModelStudent(questionLecture.getStudent());
        return new QuestionLectureGetVM(questionLecture.getId(),
                questionLecture.getTitle(),
                questionLecture.getDescription(),
                userGetVM,
                lectureGetVM, questionLecture.getCreatedAt().toString(),
                questionLecture.getUpdatedAt().toString(),
                answers);
    }
}
