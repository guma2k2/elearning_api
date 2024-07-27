package com.backend.elearning.domain.course;

public record CourseLearningVm(CourseVM course, Long sectionId, Long curriculumId, Integer secondWatched, String type) {
}
