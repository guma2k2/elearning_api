package com.backend.elearning.domain.user;

import com.backend.elearning.domain.course.CourseListGetVM;

import java.util.List;

public record UserProfileVM(
        Long id,
        String fullName,
        String photo,
        Double averageRating,
        int numberOfReview,
        int numberOfStudent,
        int numberOfCourse,
        List<CourseListGetVM> courses
) {

    public static UserProfileVM fromModel(User user, Double averageRating,
                                          int numberOfReview,
                                          int numberOfStudent,
                                          int numberOfCourse,
                                          List<CourseListGetVM> courses) {
        String fullName = user.getFirstName() + " " + user.getLastName();
        return new UserProfileVM(user.getId(), fullName, user.getPhoto(), averageRating, numberOfReview, numberOfStudent, numberOfCourse, courses);
    }
}
