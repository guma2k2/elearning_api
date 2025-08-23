package com.backend.elearning.service;

import com.backend.elearning.domain.coupon.Coupon;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.review.*;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;
    private ReviewService reviewService;

    @BeforeEach
    void beforeEach() {
        reviewService = new ReviewServiceImpl(reviewRepository, studentRepository, courseRepository);
        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    void createReviewForProduct_shouldReturnReviewVM_whenReviewIsCreatedSuccessfully() {
        // given
        String email = "student@example.com";
        ReviewPostVM reviewPostVM = new ReviewPostVM(1L, "Great course!", 5);

        Student student = Student.builder()
                .id(1L)
                .email(email)
                .build();

        Course course = Course.builder()
                .id(reviewPostVM.courseId())
                .title("Sample Course")
                .build();

        Review reviewToSave = Review.builder()
                .student(student)
                .course(course)
                .content(reviewPostVM.content())
                .ratingStar(reviewPostVM.ratingStar())
                .status(ReviewStatus.UNDER_REVIEW)
                .build();

        Review savedReview = Review.builder()
                .id(1L)
                .student(student)
                .course(course)
                .status(ReviewStatus.UNDER_REVIEW)
                .content(reviewPostVM.content())
                .ratingStar(reviewPostVM.ratingStar())
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(student));
        when(courseRepository.findById(reviewPostVM.courseId())).thenReturn(Optional.of(course));
        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(savedReview);

        // when
        ReviewVM result = reviewService.createReviewForProduct(reviewPostVM);

        // then
        assertEquals(savedReview.getId(), result.id());
        assertEquals(savedReview.getContent(), result.content());
        assertEquals(savedReview.getRatingStar(), result.ratingStar());
        verify(reviewRepository, times(1)).save(ArgumentMatchers.any(Review.class));
    }

    @Test
    void updateReview_shouldReturnUpdatedReviewVM_whenReviewIsUpdatedSuccessfully() {
        // given
        Long reviewId = 1L;
        ReviewPostVM reviewPostVM = new ReviewPostVM(1L, "Updated content", 4);

        Student student = Student.builder()
                .id(1L)
                .email("email")
                .build();

        Course course = Course.builder()
                .id(reviewPostVM.courseId())
                .title("Sample Course")
                .build();
        Review existingReview = Review.builder()
                .id(reviewId)
                .content("Original content")
                .student(student)
                .status(ReviewStatus.UNDER_REVIEW)
                .course(course)
                .ratingStar(5)
                .build();

        Review updatedReview = Review.builder()
                .id(reviewId)
                .student(student)
                .course(course)
                .status(ReviewStatus.UNDER_REVIEW)
                .content(reviewPostVM.content())
                .ratingStar(reviewPostVM.ratingStar())
                .build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(updatedReview);

        // when
        ReviewVM result = reviewService.updateReview(reviewPostVM, reviewId);

        // then
        assertEquals(updatedReview.getId(), result.id());
        assertEquals(updatedReview.getContent(), result.content());
        assertEquals(updatedReview.getRatingStar(), result.ratingStar());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }
}
