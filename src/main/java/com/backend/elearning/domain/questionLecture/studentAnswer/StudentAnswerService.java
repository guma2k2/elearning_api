package com.backend.elearning.domain.questionLecture.studentAnswer;

import com.backend.elearning.domain.questionLecture.AnswerLecture;

public interface StudentAnswerService {
    AnswerLecture create(StudentAnswerPostVM studentAnswerPostVM);
}
