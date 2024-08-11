package com.backend.elearning.domain.learning.learningCourse;

import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.course.CourseService;
import com.backend.elearning.domain.learning.learningLecture.LearningLectureRepository;
import com.backend.elearning.domain.learning.learningQuiz.LearningQuizRepository;
import com.backend.elearning.domain.review.Review;
import com.backend.elearning.domain.review.ReviewGetVM;
import com.backend.elearning.domain.review.ReviewRepository;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LearningCourseServiceImpl implements LearningCourseService {

    private final ReviewRepository reviewRepository;

    private final CourseService courseService;

    private final LearningCourseRepository learningCourseRepository;
    private final CourseRepository courseRepository;

    private final LearningLectureRepository learningLectureRepository;
    private final LearningQuizRepository learningQuizRepository ;
    private final StudentRepository studentRepository;

    public LearningCourseServiceImpl(ReviewRepository reviewRepository, CourseService courseService, LearningCourseRepository learningCourseRepository, CourseRepository courseRepository, LearningLectureRepository learningLectureRepository, LearningQuizRepository learningQuizRepository, StudentRepository studentRepository) {
        this.reviewRepository = reviewRepository;
        this.courseService = courseService;
        this.learningCourseRepository = learningCourseRepository;
        this.courseRepository = courseRepository;
        this.learningLectureRepository = learningLectureRepository;
        this.learningQuizRepository = learningQuizRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public List<LearningCourseVM> getByStudent() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<LearningCourse> learningCourses = learningCourseRepository.findByStudentEmail(email);
        List<LearningCourseVM> learningCourseVMS = learningCourses.stream().map(learningCourse -> {
            Course course = learningCourse.getCourse();
            Long courseId = course.getId();
            Optional<Review> reviewOptional = reviewRepository.findByStudentAndCourse(email, courseId);
            CourseListGetVM courseListGetVM = courseService.getCourseListGetVMById(courseId);
            int totalCurriculumCourse = courseListGetVM.totalLectures();
            Long learningLecture = learningLectureRepository.countByCourseAndStudent(email, courseId);
            Long learningQuiz = learningQuizRepository.countByCourseAndStudent(email, courseId);
            int percentFinished = (int) (((double) (learningLecture + learningQuiz) / totalCurriculumCourse) * 100);
            if (!reviewOptional.isPresent()) {
                return new LearningCourseVM(learningCourse.getId(), null, courseListGetVM, percentFinished);
            }
            ReviewGetVM reviewGetVM = ReviewGetVM.fromModel(reviewOptional.get());
            return new LearningCourseVM(learningCourse.getId(), reviewGetVM, courseListGetVM, percentFinished);

        }).toList();
        return learningCourseVMS;
    }

    @Override
    public void createLearningCourseForStudent(Long courseId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info(email);
        if (!learningCourseRepository.findByStudentAndCourse(email, courseId).isPresent()) {
            Student student = studentRepository.findByEmail(email).orElseThrow();
            Course course = courseRepository.findById(courseId).orElseThrow();
            LearningCourse learningCourse = LearningCourse.builder()
                    .student(student)
                    .course(course)
                    .build();
            learningCourseRepository.save(learningCourse);
        }
    }


}
