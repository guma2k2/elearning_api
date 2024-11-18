package com.backend.elearning.domain.review_classroom;

import com.backend.elearning.domain.review.Review;
import com.backend.elearning.domain.review.ReviewVM;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.user.UserGetVM;
import com.backend.elearning.utils.DateTimeUtils;

public record ReviewClassroomVM (Long id,
                                 String content,
                                 int ratingStar,
                                 UserGetVM student,
                                 String createdAt,
                                 String updatedAt,
                                 boolean status
){

    public static ReviewClassroomVM fromModel(ReviewClassroom review) {
        Student student = review.getStudent();
        String createdAt = review.getCreatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(review.getCreatedAt()) : "";
        String updatedAt = review.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(review.getUpdatedAt()) : "";
        return new ReviewClassroomVM(review.getId(), review.getContent(), review.getRatingStar(),
                UserGetVM.fromModelStudent(student), createdAt, updatedAt, review.isStatus());
    }
}
