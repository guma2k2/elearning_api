package com.backend.elearning.domain.reviewClassroom;

import com.backend.elearning.domain.classroom.Classroom;
import com.backend.elearning.domain.classroom.ClassroomRepository;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.exception.NotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ReviewClassroomServiceImpl implements ReviewClassroomService{

    private final ReviewClassroomRepo reviewClassroomRepo;

    private final StudentRepository studentRepository;

    private final ClassroomRepository classroomRepository;

    public ReviewClassroomServiceImpl(ReviewClassroomRepo reviewClassroomRepo, StudentRepository studentRepository, ClassroomRepository classroomRepository) {
        this.reviewClassroomRepo = reviewClassroomRepo;
        this.studentRepository = studentRepository;
        this.classroomRepository = classroomRepository;
    }

    @Override
    public ReviewClassroomVM create(ReviewClassroomPostVM reviewPost) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByEmail(email).orElseThrow();
        Classroom classroom = classroomRepository.findById(reviewPost.classroomId()).orElseThrow();
        ReviewClassroom review = ReviewClassroom
                .builder()
                .student(student)
                .classroom(classroom)
                .content(reviewPost.content())
                .ratingStar(reviewPost.ratingStar())
                .build();
        ReviewClassroom savedReview = reviewClassroomRepo.save(review);
        return ReviewClassroomVM.fromModel(savedReview);
    }

    @Override
    public ReviewClassroomVM updateReview(ReviewClassroomPostVM reviewPost, Long reviewId) {
        ReviewClassroom review = reviewClassroomRepo.findById(reviewId).orElseThrow();
        review.setRatingStar(reviewPost.ratingStar());
        review.setContent(reviewPost.content());
        review.setUpdatedAt(LocalDateTime.now());
        ReviewClassroom updatedReview = reviewClassroomRepo.save(review);
        return ReviewClassroomVM.fromModel(updatedReview);
    }

    @Override
    public List<ReviewClassroomVM> findByClassroomId(Long classroomId) {

        List<ReviewClassroom> reviewClassrooms = reviewClassroomRepo.findByClassroom(classroomId);

        return reviewClassrooms.stream().map(reviewClassroom -> ReviewClassroomVM.fromModel(reviewClassroom)).toList();
    }

    @Override
    public ReviewClassroomVM getByStudent() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ReviewClassroom review = reviewClassroomRepo.findByStudent(email).orElseThrow(() -> new NotFoundException(""));
        return ReviewClassroomVM.fromModel(review);
    }
}
