package com.backend.elearning.domain.course;
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
         String level,
         Long price,
         String image,
         boolean free,
         Integer categoryId,
         Integer topicId

) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoursePostVM that = (CoursePostVM) o;
        return free == that.free &&
                Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(headline, that.headline) &&
                Arrays.equals(objectives, that.objectives) &&
                Arrays.equals(requirements, that.requirements) &&
                Arrays.equals(targetAudiences, that.targetAudiences) &&
                Objects.equals(description, that.description) &&
                Objects.equals(level, that.level) &&
                Objects.equals(price, that.price) &&
                Objects.equals(image, that.image) &&
                Objects.equals(categoryId, that.categoryId) &&
                Objects.equals(topicId, that.topicId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, headline, description, level, price, image, free, categoryId, topicId);
        result = 31 * result + Arrays.hashCode(objectives);
        result = 31 * result + Arrays.hashCode(requirements);
        result = 31 * result + Arrays.hashCode(targetAudiences);
        return result;
    }

    @Override
    public String toString() {
        return "CoursePostVM{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", headline='" + headline + '\'' +
                ", objectives=" + Arrays.toString(objectives) +
                ", requirements=" + Arrays.toString(requirements) +
                ", targetAudiences=" + Arrays.toString(targetAudiences) +
                ", description='" + description + '\'' +
                ", level='" + level + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", free=" + free +
                ", categoryId=" + categoryId +
                ", topicId=" + topicId +
                '}';
    }
}
