package com.backend.elearning.domain.course;

import com.backend.elearning.domain.section.SectionVM;

import java.util.List;

public record CourseVM(
        Long id,

        String title,

        String headline,

        String[] objectives,

        String[] requirements,

        String[] targetAudiences,
        String description,
        String level,
        String imageURL,
        String createdAt,
        String updatedAt,
        boolean free,
        boolean isPublish,
        Integer categoryId,
        Integer topicId,
        int ratingCount,
        double averageRating,
        int totalLectureCourse,
        String totalDurationCourse,
        List<SectionVM> sections
) {
    public static CourseVM fromModel (Course course, List<SectionVM> sections, int ratingCount,
                                      double averageRating,
                                      int totalLectureCourse,
                                      String totalDurationCourse) {
        String level = course.getLevel() != null ? course.getLevel().toString() : "";
        return new CourseVM(course.getId(), course.getTitle(), course.getHeadline(), course.getObjectives(), course.getRequirements(),
                course.getTargetAudiences(), course.getDescription(),level, course.getImageId(), course.getCreatedAt().toString(), course.getUpdatedAt().toString() ,course.isFree(), course.isPublish(), course.getCategory().getId(), course.getTopic().getId(),
                ratingCount, averageRating, totalLectureCourse, totalDurationCourse,
                sections);
    }
}
