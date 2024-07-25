package com.backend.elearning.domain.review;

import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserGetVM;

public record ReviewVM(
        Long id,
        String content,
        int rating,
        UserGetVM student,
        String created_at,
        String updated_at,
        boolean status
) {

    public static ReviewVM fromModel(Review review) {
        Student student = review.getStudent();
        String updatedAt = review.getUpdatedAt() == null ? review.getCreatedAt().toString() : review.getUpdatedAt().toString();
        return new ReviewVM(review.getId(), review.getContent(), review.getRatingStar(),
                UserGetVM.fromModelStudent(student), review.getCreatedAt().toString(), updatedAt, review.isStatus());
    }
}
