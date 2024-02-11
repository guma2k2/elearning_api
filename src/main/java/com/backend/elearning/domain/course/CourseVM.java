package com.backend.elearning.domain.course;

import com.backend.elearning.domain.section.SectionVM;

import java.util.List;

public record CourseVM(
        Long id,

        String title,

        String headline,

        String objective,

        String requirement,

        String targetAudience,

        String description,

        String imageURL,

        boolean free,
        Integer categoryId,
        Integer topicId,
        List<SectionVM> sections
) {
    public static CourseVM fromModel (Course course, String imageURL, List<SectionVM> sections) {
        return new CourseVM(course.getId(), course.getTitle(), course.getHeadline(), course.getObjective(), course.getRequirement(),
                course.getTargetAudience(), course.getDescription(), imageURL, course.isFree(),course.getCategory().getId(), course.getTopic().getId(), sections);
    }
}
