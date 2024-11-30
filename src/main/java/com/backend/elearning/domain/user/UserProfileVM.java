package com.backend.elearning.domain.user;

import com.backend.elearning.domain.course.CourseListGetVM;
import org.apache.commons.math3.dfp.DfpField;

import java.math.BigDecimal;
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

        Double newAvgRating = BigDecimal.valueOf(averageRating)
                .setScale(1, DfpField.RoundingMode.ROUND_HALF_UP.ordinal())
                .doubleValue();
        return new UserProfileVM(user.getId(), fullName, user.getPhoto(), newAvgRating, numberOfReview, numberOfStudent, numberOfCourse, courses);
    }
}
