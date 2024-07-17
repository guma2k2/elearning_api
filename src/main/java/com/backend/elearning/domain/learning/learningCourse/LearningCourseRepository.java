package com.backend.elearning.domain.learning.learningCourse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningCourseRepository extends JpaRepository<LearningCourse, Long> {
}
