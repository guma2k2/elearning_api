package com.backend.elearning.domain.questionLecture.userAnswer;

import com.backend.elearning.domain.questionLecture.AnswerLecture;

public interface UserAnswerService {

    AnswerLecture create(UserAnswerPostVM userAnswerPostVM);
}
