package com.backend.elearning.domain.course;

public record CoursePostVM(
         Long id,

         String title,

         String headline,

         String[] objectives,

         String[] requirements,

         String[] targetAudiences,

         String description,
         String level,
         Long price,
         String image,
         boolean free,
         Integer categoryId,
         Integer topicId

) {
}
