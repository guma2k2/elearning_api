package com.backend.elearning.domain.course;

import com.backend.elearning.domain.user.User;

public record CourseListGetVM(
        Long id,
        String title,
        String headline,
        String level,
        String totalDurationCourse,
        int totalLectures,
        double averageRating,
        int ratingCount,
        String image,
        double price,
        String createdBy
) {

    public static CourseListGetVM fromModel(
            Course course,
            String totalDurationCourse,
            int totalLectures,
            double averageRating,
            int ratingCount
    ) {
        User user = course.getUser();
        String fullName = user.getFirstName() + " " + user.getLastName();
        return new CourseListGetVM(course.getId(), course.getTitle(), course.getHeadline() ,course.getLevel().toString(), totalDurationCourse, totalLectures, averageRating, ratingCount, course.getImageId(), course.getPrice(), fullName);
    }
}
