package com.backend.elearning.domain.section;

import jakarta.validation.constraints.NotEmpty;

public record SectionPostVM(
        Long id,
        @NotEmpty
        String title,
        float number,
        String objective,
        Long courseId
) {
}
