package com.backend.elearning.domain.user;

public record UserProfileVM(
        Long id,
        String fullName,
        String photo,
        Double averageRating,
        int numberOfReview,
        int numberOfStudent,
        int numberOfCourse
) {

    public static UserProfileVM fromModel(User user, Double averageRating,
                                          int numberOfReview,
                                          int numberOfStudent,
                                          int numberOfCourse) {
        String fullName = user.getFirstName() + " " + user.getLastName();
        return new UserProfileVM(user.getId(), fullName, user.getPhoto(), averageRating, numberOfReview, numberOfStudent, numberOfCourse);
    }
}
