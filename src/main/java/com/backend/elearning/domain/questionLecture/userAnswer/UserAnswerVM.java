package com.backend.elearning.domain.questionLecture.userAnswer;

import com.backend.elearning.domain.questionLecture.studentAnswer.StudentAnswerVM;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserGetVM;

public record UserAnswerVM(
        Long id,
        String content,
        String createdAt,
        String updatedAt,
        UserGetVM user
) {

    public static final UserAnswerVM fromModel(UserAnswer userAnswer) {
        User user = userAnswer.getUser();
        UserGetVM userGetVM = UserGetVM.fromModel(user);
        return new UserAnswerVM(userAnswer.getId(), userAnswer.getContent(), userAnswer.getCreatedAt().toString(),
                userAnswer.getUpdatedAt().toString(), userGetVM);
    }
}
