package com.backend.elearning.domain.reviewClassroom;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ReviewClassroomController {

    private final ReviewClassroomService reviewClassroomService ;

    public ReviewClassroomController(ReviewClassroomService reviewClassroomService) {
        this.reviewClassroomService = reviewClassroomService;
    }

    @PostMapping("/review-classrooms")
    public ResponseEntity<ReviewClassroomVM> createReview(@Valid @RequestBody ReviewClassroomPostVM reviewPostVM){
        ReviewClassroomVM review = reviewClassroomService.create(reviewPostVM);
        return ResponseEntity.ok().body(review);
    }


    @PutMapping("/review-classrooms/{id}")
    public ResponseEntity<ReviewClassroomVM> updateReview(@Valid @RequestBody ReviewClassroomPostVM reviewPostVM, @PathVariable("id") Long reviewId){
        ReviewClassroomVM updatedReview = reviewClassroomService.updateReview(reviewPostVM, reviewId);
        return ResponseEntity.ok().body(updatedReview);
    }

    @GetMapping("/review-classrooms/classroom/{classroomId}")
    public ResponseEntity<List<ReviewClassroomVM>> getByClassroomId(@PathVariable("classroomId") Long classroomId){
        List<ReviewClassroomVM> reviewClassroomVMS = reviewClassroomService.findByClassroomId(classroomId);
        return ResponseEntity.ok().body(reviewClassroomVMS);
    }
    @GetMapping("/review-classrooms/student")
    public ResponseEntity<ReviewClassroomVM> getByStudent(){
        ReviewClassroomVM review = reviewClassroomService.getByStudent();
        return ResponseEntity.ok().body(review);
    }
}
