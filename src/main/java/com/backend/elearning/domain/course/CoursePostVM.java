package com.backend.elearning.domain.course;
import com.backend.elearning.utils.EnumPattern;
import jakarta.validation.constraints.NotEmpty;

import java.util.Arrays;
import java.util.Objects;

public record CoursePostVM(
         Long id,
         @NotEmpty(message = "course title must not be empty")
         String title,
         String headline,

        String[] objectives,

        String[] requirements,

        String[] targetAudiences,

         String description,

         @EnumPattern(name = "level", regexp = "Beginner|Intermediate|Expert|AllLevel")
         ELevel level,
         Long price,
         String image,
         boolean free,
         Integer categoryId,
         Integer topicId

) {
}
