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
}
