package com.backend.elearning.domain.course;

import com.backend.elearning.domain.section.SectionVM;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserProfileVM;
import com.backend.elearning.utils.DateTimeUtils;

import java.util.List;
import java.util.Arrays;
import java.util.Objects;

public record CourseVM(
        Long id,
        String title,
        String headline,
        String slug,
        String[] objectives,
        String[] requirements,
        String[] targetAudiences,
        String description,
        String level,
        String image,
        String createdAt,
        String updatedAt,
        boolean free,
        Long price,
        String status,
        Integer categoryId,
        Integer topicId,
        int ratingCount,
        double averageRating,
        int totalLectureCourse,
        String totalDurationCourse,
        String createdBy,
        List<SectionVM> sections,
        UserProfileVM user,
        boolean learning,
        String breadcrumb
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseVM that = (CourseVM) o;
        return free == that.free &&
                status == that.status &&
                ratingCount == that.ratingCount &&
                Double.compare(that.averageRating, averageRating) == 0 &&
                totalLectureCourse == that.totalLectureCourse &&
                learning == that.learning &&
                Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(headline, that.headline) &&
                Objects.equals(slug, that.slug) &&
                Arrays.equals(objectives, that.objectives) &&
                Arrays.equals(requirements, that.requirements) &&
                Arrays.equals(targetAudiences, that.targetAudiences) &&
                Objects.equals(description, that.description) &&
                Objects.equals(level, that.level) &&
                Objects.equals(image, that.image) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(price, that.price) &&
                Objects.equals(categoryId, that.categoryId) &&
                Objects.equals(topicId, that.topicId) &&
                Objects.equals(totalDurationCourse, that.totalDurationCourse) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(sections, that.sections) &&
                Objects.equals(user, that.user) &&
                Objects.equals(breadcrumb, that.breadcrumb);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, headline, slug, description, level, image, createdAt, updatedAt, free,
                price, status, categoryId, topicId, ratingCount, averageRating, totalLectureCourse,
                totalDurationCourse, createdBy, sections, user, learning, breadcrumb);
        result = 31 * result + Arrays.hashCode(objectives);
        result = 31 * result + Arrays.hashCode(requirements);
        result = 31 * result + Arrays.hashCode(targetAudiences);
        return result;
    }

    @Override
    public String toString() {
        return "CourseVM{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", headline='" + headline + '\'' +
                ", slug='" + slug + '\'' +
                ", objectives=" + Arrays.toString(objectives) +
                ", requirements=" + Arrays.toString(requirements) +
                ", targetAudiences=" + Arrays.toString(targetAudiences) +
                ", description='" + description + '\'' +
                ", level='" + level + '\'' +
                ", image='" + image + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", free=" + free +
                ", price=" + price +
                ", isPublish=" + status.toString() +
                ", categoryId=" + categoryId +
                ", topicId=" + topicId +
                ", ratingCount=" + ratingCount +
                ", averageRating=" + averageRating +
                ", totalLectureCourse=" + totalLectureCourse +
                ", totalDurationCourse='" + totalDurationCourse + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", sections=" + sections +
                ", user=" + user +
                ", learning=" + learning +
                ", breadcrumb='" + breadcrumb + '\'' +
                '}';
    }
    public static CourseVM fromModel (Course course, List<SectionVM> sections, int ratingCount,
                                      double averageRating,
                                      int totalLectureCourse,
                                      String totalDurationCourse,
                                      UserProfileVM userProfileVM,
                                      boolean learning) {
        String level = course.getLevel() != null ? course.getLevel().toString() : "";
        String topicName = course.getTopic().getName();
        String categoryChildName = course.getCategory().getName();
        String categoryParentName = course.getCategory().getParent() != null ? course.getCategory().getParent().getName() : "";
        String breadcrumb = categoryParentName.concat("-").concat(categoryChildName).concat("-").concat(topicName);
        User user = course.getUser();
        String createdBy = user.getFirstName() + " " + user.getLastName();
        Long price = course.getPrice() != null ? course.getPrice() : 0L;
        String createdAt = course.getCreatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(course.getCreatedAt()) : "";
        String updatedAt = course.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToString(course.getUpdatedAt()) : "";
        return new CourseVM(course.getId(), course.getTitle(), course.getHeadline(), course.getSlug(), course.getObjectives(), course.getRequirements(),
                course.getTargetAudiences(),
                course.getDescription(),level,
                course.getImageId(),
                createdAt,
                updatedAt,
                course.isFree(),
                price,
                course.getStatus().name(),
                course.getCategory().getId(),
                course.getTopic().getId(),
                ratingCount,
                averageRating,
                totalLectureCourse,
                totalDurationCourse,
                createdBy,
                sections, userProfileVM,
                learning,
                breadcrumb);
    }
}
