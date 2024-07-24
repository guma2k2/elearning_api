package com.backend.elearning.domain.review;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.coupon.Coupon;
import com.backend.elearning.domain.coupon.CouponVM;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;


    public ReviewServiceImpl(ReviewRepository reviewRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.reviewRepository = reviewRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    private final static String sortField = "createdAt";





    @Override
    public ReviewVM createReviewForProduct(ReviewPostVM reviewPost) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByEmail(email).orElseThrow();
        Course course = courseRepository.findById(reviewPost.courseId()).orElseThrow();
        Review review = Review
                .builder()
                .student(student)
                .course(course)
                .content(reviewPost.content())
                .ratingStar(reviewPost.ratingStar())
                .createdAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);
        return ReviewVM.fromModel(savedReview);
    }

    @Override
    public PageableData<ReviewVM> getByMultiQuery(Long courseId, Integer pageNum, int pageSize, Integer ratingStar, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("desc") ? sort.descending() : sort.ascending();
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Review> reviewPage = null;
        if (ratingStar != null) {
            reviewPage = reviewRepository.findByRatingStarAndCourseId(ratingStar, courseId, pageable);
        }else {
            reviewPage = reviewRepository.findByCourseId(courseId, pageable);
        }
        List<Review> reviews = reviewPage.getContent();

        int totalPages = reviewPage.getTotalPages();
        long totalElements = reviewPage.getTotalElements();

        List<ReviewVM> reviewDtos = reviews.stream().map(ReviewVM::fromModel).toList();

        return new PageableData<>(pageNum, pageSize, totalElements, totalPages, reviewDtos);
    }

    @Override
    public ReviewVM updateReview(ReviewPostVM reviewPostVM, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        review.setRatingStar(reviewPostVM.ratingStar());
        review.setContent(reviewPostVM.content());
        Review updatedReview = reviewRepository.save(review);
        return ReviewVM.fromModel(updatedReview);
    }

    @Override
    public List<Review> findByCourseId(Long courseId) {
        return reviewRepository.findByCourseId(courseId);
    }

    @Override
    public PageableData<ReviewVM> getPageableReviews(int pageNum, int pageSize) {
        List<ReviewVM> reviewVMS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        Page<Review> reviewPage = reviewRepository.findAllCustom(pageable);
        List<Review> reviews = reviewPage.getContent();
        for (Review review : reviews) {
            reviewVMS.add(ReviewVM.fromModel(review));
        }
        return new PageableData(
                pageNum,
                pageSize,
                (int) reviewPage.getTotalElements(),
                reviewPage.getTotalPages(),
                reviewVMS
        );
    }
}
