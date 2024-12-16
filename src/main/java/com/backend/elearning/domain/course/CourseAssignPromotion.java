package com.backend.elearning.domain.course;

public record CourseAssignPromotion(
        Long id,
        String title,
        boolean assigned
) {

    public static CourseAssignPromotion fromModel(Course course, boolean assigned) {
        return new CourseAssignPromotion(course.getId(), course.getTitle(), assigned);
    }
}
