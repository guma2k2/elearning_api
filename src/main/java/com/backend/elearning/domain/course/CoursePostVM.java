package com.backend.elearning.domain.course;

public record CoursePostVM(
         Long id,

         String title,

         String headline,

         String objective,

         String requirement,

         String targetAudience,

         String description,
         String level,
         String imageId,
         boolean free,
         Integer categoryId,
         Integer topicId

) {
}
