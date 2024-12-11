package com.backend.elearning.domain.questionLecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionLectureRepo extends JpaRepository<QuestionLecture, Long> {
}
