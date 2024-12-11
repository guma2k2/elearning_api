package com.backend.elearning.domain.questionLecture.studentAnswer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAnswerRepo extends JpaRepository<StudentAnswer, Long> {
}
