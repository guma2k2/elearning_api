package com.backend.elearning.domain.learning.learningCourse;

import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.review.ReviewGetVM;

public record LearningCourseVM(
        Long id,
        ReviewGetVM review,
        CourseListGetVM course,
        int percentFinished
) {
}
