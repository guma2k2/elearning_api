package com.backend.elearning.domain.course;
public record CourseGetVM(
        Long id,
        String title,
        String headline,
        String description,
        String level,
        String image
) {

    public static CourseGetVM fromModel(Course course) {
        return new CourseGetVM(course.getId(), course.getTitle(), course.getHeadline(), course.getDescription(),
                course.getLevel().toString(), course.getImageId());
    }
}
