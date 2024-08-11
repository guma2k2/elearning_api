package com.backend.elearning.domain.review;

import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserGetVM;
import com.backend.elearning.utils.DateTimeUtils;

public record ReviewVM(
        Long id,
        String content,
        int ratingStar,
        UserGetVM student,
        String created_at,
        String updated_at,
        boolean status
) {

    public static ReviewVM fromModel(Review review) {
        Student student = review.getStudent();
        String createdAt = review.getCreatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(review.getCreatedAt()) : "";
        String updatedAt = review.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(review.getUpdatedAt()) : "";
        return new ReviewVM(review.getId(), review.getContent(), review.getRatingStar(),
                UserGetVM.fromModelStudent(student), createdAt, updatedAt, review.isStatus());
    }
}
