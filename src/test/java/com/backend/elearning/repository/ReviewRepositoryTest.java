package com.backend.elearning.repository;

import com.backend.elearning.TestConfig;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.review.Review;
import com.backend.elearning.domain.review.ReviewRepository;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import({TestConfig.class})
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        // Clearing the repositories before each test
        reviewRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();

        // Setting up test data
        Student student1 = Student.builder()
                .email("student1@example.com")
                .build();

        Student student2 = Student.builder()
                .email("student2@example.com")
                .build();

        Course course1 = Course.builder()
                .title("Course One")
                .build();

        Course course2 = Course.builder()
                .title("Course Two")
                .build();

        studentRepository.saveAll(List.of(student1, student2));
        courseRepository.saveAll(List.of(course1, course2));

        Review review1 = Review.builder()
                .content("Great course!")
                .ratingStar(5)
                .course(course1)
                .student(student1)
                .build();

        Review review2 = Review.builder()
                .content("Good course, but could be better.")
                .ratingStar(4)
                .course(course1)
                .student(student2)
                .build();

        Review review3 = Review.builder()
                .content("Not worth the money.")
                .ratingStar(2)
                .course(course2)
                .student(student1)
                .build();

        reviewRepository.saveAll(List.of(review1, review2, review3));
    }


    @Test
    void testFindTotalReviews_WithoutEmail() {
        // Given
        String email = null;  // No email filter

        // When
        long totalReviews = reviewRepository.findTotalReviews(email);

        // Then
        assertEquals(0, totalReviews, "There should be 3 total reviews for all students");
    }

    @Test
    void testCountByRatingAndCourse_WithExistingRating() {
        // Given
        Long courseId = courseRepository.findAll().get(0).getId();
        int ratingStar = 5;

        // When
        Long count = reviewRepository.countByRatingAndCourse(ratingStar, courseId);

        // Then
        assertEquals(1, count, "There should be 1 review with the given rating for the specified course");
    }

    @Test
    void testCountByRatingAndCourse_WithNonExistingRating() {
        // Given
        Long courseId = courseRepository.findAll().get(1).getId();  // Assuming course 2
        int ratingStar = 5;

        // When
        Long count = reviewRepository.countByRatingAndCourse(ratingStar, courseId);

        // Then
        assertEquals(0, count, "There should be 0 reviews with the given rating for the specified course");
    }
}
