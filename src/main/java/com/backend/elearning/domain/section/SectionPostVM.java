package com.backend.elearning.domain.section;

public record SectionPostVM(
        Long id,
        String title,
        int number,
        String objective,
        Long courseId
) {
}
