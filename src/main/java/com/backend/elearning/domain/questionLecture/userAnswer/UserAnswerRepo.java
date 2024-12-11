package com.backend.elearning.domain.questionLecture.userAnswer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAnswerRepo extends JpaRepository<UserAnswer, Long> {
}
