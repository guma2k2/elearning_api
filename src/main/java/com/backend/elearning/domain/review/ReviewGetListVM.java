package com.backend.elearning.domain.review;

import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseGetVM;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.user.UserGetVM;
import com.backend.elearning.utils.DateTimeUtils;

public record ReviewGetListVM(
        Long id,
        String content,
        int ratingStar,
        UserGetVM student,
        CourseGetVM course,
        String createdAt,
        String updatedAt,
        boolean status
) {

    public static ReviewGetListVM fromModel(Review review) {
        Student student = review.getStudent();
        Course course = review.getCourse();
        CourseGetVM courseGetVM = CourseGetVM.fromModel(course);
        String createdAt = review.getCreatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(review.getCreatedAt()) : "";
        String updatedAt = review.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(review.getUpdatedAt()) : "";
        return new ReviewGetListVM(review.getId(), review.getContent(), review.getRatingStar(),
                UserGetVM.fromModelStudent(student),courseGetVM , createdAt, updatedAt, review.isStatus());
    }
}
