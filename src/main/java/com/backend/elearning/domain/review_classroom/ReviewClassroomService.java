package com.backend.elearning.domain.review_classroom;



import java.util.List;

public interface ReviewClassroomService {

    ReviewClassroomVM create(ReviewClassroomPostVM reviewPost);
    ReviewClassroomVM updateReview(ReviewClassroomPostVM reviewPost, Long reviewId);
    List<ReviewClassroomVM> findByClassroomId(Long classroomId);

    ReviewClassroomVM getByStudent();
}
