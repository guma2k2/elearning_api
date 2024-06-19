package com.backend.elearning.domain.course;

public record CourseListGetVM(
        Long id,
        String title,
        String level,
        int totalHours,
        int lectureCount,
        float averageRating,
        int ratingCount,
        String image
) {

    public static CourseListGetVM fromModel(Course course, int totalHours, int lectureCount,
                                            float averageRating,
                                            int ratingCount) {
        return new CourseListGetVM(course.getId(), course.getTitle(), course.getLevel().toString(), totalHours, lectureCount, averageRating, ratingCount, course.getImageId());
    }
}
