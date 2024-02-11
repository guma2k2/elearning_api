package com.backend.elearning.domain.quiz;

public record QuizPostVM (
        Long id,
        String title,
        float number,
        String description,
        Long sectionId
) {
}
