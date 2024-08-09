package com.backend.elearning.domain.course;

import com.backend.elearning.domain.user.User;

public record CourseListGetVM(
        Long id,
        String title,
        String headline,
        String level,
        String slug,
        String totalDurationCourse,
        int totalLectures,
        double averageRating,
        int ratingCount,
        String image,
        Long price,
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
        Long price = course.getPrice() != null ? course.getPrice() : 0L;

        return new CourseListGetVM(course.getId(), course.getTitle(), course.getHeadline() ,course.getLevel().toString(), course.getSlug(), totalDurationCourse, totalLectures, averageRating, ratingCount, course.getImageId(), price, fullName);
    }
}
