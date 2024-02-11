package com.backend.elearning.domain.section;

public record SectionPostVM(
        Long id,
        String title,
        float number,
        String objective,
        Long courseId
) {
}
