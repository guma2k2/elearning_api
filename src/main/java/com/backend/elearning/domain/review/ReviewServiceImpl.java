package com.backend.elearning.domain.review;

import com.backend.elearning.domain.common.PageableData;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;

    private final static int fiveStar = 5;
    private final static int fourStar = 4;
    private final static int threeStar = 3;
    private final static int twoStar = 2;
    private final static int oneStar = 1;


    private final static String sortField = "createdAt";
    public ReviewServiceImpl(ReviewRepository reviewRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.reviewRepository = reviewRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }



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
                .updatedAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);
        return ReviewVM.fromModel(savedReview);
    }

    @Override
    public PageableDataReview<ReviewVM> getByMultiQuery(Long courseId, Integer pageNum, int pageSize, Integer ratingStar, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("desc") ? sort.descending() : sort.ascending();
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        long ratingFiveStarCount = reviewRepository.countByRatingAndCourse(fiveStar, courseId);
        long ratingFourStarCount = reviewRepository.countByRatingAndCourse(fourStar, courseId);
        long ratingThreeStarCount = reviewRepository.countByRatingAndCourse(threeStar, courseId);
        long ratingTwoStarCount = reviewRepository.countByRatingAndCourse(twoStar, courseId);
        long ratingOneStarCount = reviewRepository.countByRatingAndCourse(oneStar, courseId);
        long sum = ratingOneStarCount + ratingTwoStarCount + ratingThreeStarCount + ratingFourStarCount + ratingFiveStarCount;

        int percentFiveStar = (int) (sum != 0 ?  (ratingFiveStarCount * 100 / sum) : 0);
        int percentFourStar = (int) (sum != 0 ?  ratingFourStarCount * 100 / sum : 0);
        int percentThreeStar = (int) (sum != 0 ? ratingThreeStarCount * 100 / sum : 0);
        int percentTwoStar = (int) (sum != 0 ? ratingTwoStarCount * 100 / sum : 0);
        int percentOneStar = ratingOneStarCount != 0 ?  100 - percentFiveStar - percentFourStar - percentThreeStar - percentTwoStar : 0;


        Page<Review> reviewPage = null;
        if (ratingStar != null) {
            reviewPage = reviewRepository.findByRatingStarAndCourseId(ratingStar, courseId, pageable);
        }else {
            reviewPage = reviewRepository.findByCourseId(courseId, pageable);
        }
        List<Review> reviews = reviewPage.getContent();


        List<ReviewVM> reviewFilterVMS = reviews.stream().map(review -> {
            ReviewVM reviewFilterVM = ReviewVM.fromModel(review);
            return reviewFilterVM;
        }).toList();

        return new PageableDataReview<ReviewVM>(
                pageNum,
                pageSize,
                (int) reviewPage.getTotalElements(),
                reviewPage.getTotalPages(),
                reviewFilterVMS,
                percentFiveStar,
                percentFourStar,
                percentThreeStar,
                percentTwoStar,
                percentOneStar
        );
    }

    @Override
    public ReviewVM updateReview(ReviewPostVM reviewPostVM, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        review.setRatingStar(reviewPostVM.ratingStar());
        review.setContent(reviewPostVM.content());
        review.setUpdatedAt(LocalDateTime.now());
        Review updatedReview = reviewRepository.save(review);
        return ReviewVM.fromModel(updatedReview);
    }

    @Override
    public List<Review> findByCourseId(Long courseId) {
        return reviewRepository.findByCourseId(courseId);
    }

    @Override
    public PageableData<ReviewGetListVM> getPageableReviews(int pageNum, int pageSize, String keyword) {
        List<ReviewGetListVM> reviewVMS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Review> reviewPage = keyword != null ? reviewRepository.findAllCustomWithKeyword(pageable, keyword) :
                reviewRepository.findAllCustom(pageable);
        List<Review> reviews = reviewPage.getContent();
        for (Review review : reviews) {
            reviewVMS.add(ReviewGetListVM.fromModel(review));
        }
        return new PageableData(
                pageNum,
                pageSize,
                (int) reviewPage.getTotalElements(),
                reviewPage.getTotalPages(),
                reviewVMS
        );
    }

    @Override
    @Transactional
    public void updateStatusReview(boolean status, Long reviewId) {
        reviewRepository.updateStatusReview(status, reviewId);
    }
}
