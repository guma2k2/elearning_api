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
        List<SectionVM> sections
) {
    public static CourseVM fromModel (Course course, String imageURL, List<SectionVM> sections) {
        String level = course.getLevel() != null ? course.getLevel().toString() : "";
        return new CourseVM(course.getId(), course.getTitle(), course.getHeadline(), course.getObjectives(), course.getRequirements(),
                course.getTargetAudiences(), course.getDescription(),level, imageURL, course.getCreatedAt().toString(), course.getUpdatedAt().toString() ,course.isFree(), false, course.getCategory().getId(), course.getTopic().getId(), sections);
    }
}
