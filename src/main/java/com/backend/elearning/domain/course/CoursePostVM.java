package com.backend.elearning.domain.course;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.Arrays;
@EqualsAndHashCode
@ToString
public record CoursePostVM(
         Long id,

         String title,

         String headline,

         @EqualsAndHashCode.Include String[] objectives,

         @EqualsAndHashCode.Include String[] requirements,

         @EqualsAndHashCode.Include String[] targetAudiences,

         String description,
         String level,
         Long price,
         String image,
         boolean free,
         Integer categoryId,
         Integer topicId

) {
    @EqualsAndHashCode.Include
    private boolean arraysEqual(String[] array1, String[] array2) {
        return Arrays.equals(array1, array2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CoursePostVM other)) return false;

        return id.equals(other.id) &&
                title.equals(other.title) &&
                headline.equals(other.headline) &&
                arraysEqual(objectives, other.objectives) &&
                arraysEqual(requirements, other.requirements) &&
                arraysEqual(targetAudiences, other.targetAudiences) &&
                description.equals(other.description) &&
                level.equals(other.level) &&
                price.equals(other.price) &&
                image.equals(other.image) &&
                free == other.free &&
                categoryId.equals(other.categoryId) &&
                topicId.equals(other.topicId);
    }
}
