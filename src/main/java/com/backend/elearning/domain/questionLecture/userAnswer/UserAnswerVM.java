package com.backend.elearning.domain.questionLecture.userAnswer;

import com.backend.elearning.domain.questionLecture.AnswerLecture;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserGetVM;
import com.backend.elearning.utils.DateTimeUtils;

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
        String createdAt = userAnswer.getCreatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(userAnswer.getCreatedAt()) : "";
        String updatedAt = userAnswer.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(userAnswer.getUpdatedAt()) : "";
        return new UserAnswerVM(userAnswer.getId(), userAnswer.getContent(),
                createdAt,
                updatedAt, userGetVM);
    }
}
