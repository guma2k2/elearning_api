package com.backend.elearning.domain.course;

import com.backend.elearning.domain.section.SectionVM;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserProfileVM;
import com.backend.elearning.utils.DateTimeUtils;

import java.util.List;

public record CourseVM(
        Long id,
        String title,
        String headline,
        String slug,
        String[] objectives,
        String[] requirements,
        String[] targetAudiences,
        String description,
        String level,
        String image,
        String createdAt,
        String updatedAt,
        boolean free,
        Long price,
        boolean isPublish,
        Integer categoryId,
        Integer topicId,
        int ratingCount,
        double averageRating,
        int totalLectureCourse,
        String totalDurationCourse,
        String createdBy,
        List<SectionVM> sections,
        UserProfileVM user,
        boolean learning,
        String breadcrumb
) {
    public static CourseVM fromModel (Course course, List<SectionVM> sections, int ratingCount,
                                      double averageRating,
                                      int totalLectureCourse,
                                      String totalDurationCourse,
                                      UserProfileVM userProfileVM,
                                      boolean learning) {
        String level = course.getLevel() != null ? course.getLevel().toString() : "";
        String topicName = course.getTopic().getName();
        String categoryChildName = course.getCategory().getName();
        String categoryParentName = course.getCategory().getParent() != null ? course.getCategory().getParent().getName() : "";
        String breadcrumb = categoryParentName.concat("-").concat(categoryChildName).concat("-").concat(topicName);
        User user = course.getUser();
        String createdBy = user.getFirstName() + " " + user.getLastName();
        Long price = course.getPrice() != null ? course.getPrice() : 0L;
        String createdAt = course.getCreatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(course.getCreatedAt()) : "";
        String updatedAt = course.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(course.getUpdatedAt()) : "";
        return new CourseVM(course.getId(), course.getTitle(), course.getHeadline(), course.getSlug(), course.getObjectives(), course.getRequirements(),
                course.getTargetAudiences(),
                course.getDescription(),level,
                course.getImageId(),
                createdAt,
                updatedAt,
                course.isFree(),
                price,
                course.isPublish(),
                course.getCategory().getId(),
                course.getTopic().getId(),
                ratingCount,
                averageRating,
                totalLectureCourse,
                totalDurationCourse,
                createdBy,
                sections, userProfileVM,
                learning,
                breadcrumb);
    }
}
