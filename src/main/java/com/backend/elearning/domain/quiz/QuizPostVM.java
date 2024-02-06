package com.backend.elearning.domain.quiz;

public record QuizPostVM (
        Long id,
        String title,
        int number,
        String description,
        Long sectionId
) {
}
