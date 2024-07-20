package com.backend.elearning.domain.learning.learningCourse;

import java.util.List;

public interface LearningCourseService {
    List<LearningCourseVM> getByStudent();
    void createLearningCourseForStudent(Long courseId);

}
