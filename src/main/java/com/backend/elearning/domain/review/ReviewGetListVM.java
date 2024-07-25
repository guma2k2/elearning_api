package com.backend.elearning.domain.review;

import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseGetVM;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.user.UserGetVM;

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
        String updatedAt = review.getUpdatedAt() == null ? review.getCreatedAt().toString() : review.getUpdatedAt().toString();
        return new ReviewGetListVM(review.getId(), review.getContent(), review.getRatingStar(),
                UserGetVM.fromModelStudent(student),courseGetVM ,review.getCreatedAt().toString(), updatedAt, review.isStatus());
    }
}
