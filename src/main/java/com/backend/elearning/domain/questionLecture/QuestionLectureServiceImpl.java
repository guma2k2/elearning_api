package com.backend.elearning.domain.questionLecture;

import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.lecture.LectureRepository;
import com.backend.elearning.domain.questionLecture.studentAnswer.StudentAnswer;
import com.backend.elearning.domain.questionLecture.studentAnswer.StudentAnswerRepo;
import com.backend.elearning.domain.questionLecture.userAnswer.UserAnswer;
import com.backend.elearning.domain.questionLecture.userAnswer.UserAnswerRepo;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionLectureServiceImpl implements QuestionLectureService {

    private final QuestionLectureRepo questionLectureRepo;
    private final StudentRepository studentRepository;
    private final LectureRepository lectureRepository;

    private final UserAnswerRepo userAnswerRepo;
    private final StudentAnswerRepo studentAnswerRepo;

    public QuestionLectureServiceImpl(QuestionLectureRepo questionLectureRepo, StudentRepository studentRepository, LectureRepository lectureRepository, UserAnswerRepo userAnswerRepo, StudentAnswerRepo studentAnswerRepo) {
        this.questionLectureRepo = questionLectureRepo;
        this.studentRepository = studentRepository;
        this.lectureRepository = lectureRepository;
        this.userAnswerRepo = userAnswerRepo;
        this.studentAnswerRepo = studentAnswerRepo;
    }

    @Override
    public QuestionLectureVM create(QuestionLecturePostVM questionLecturePostVM) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Lecture lecture = lectureRepository.findById(questionLecturePostVM.lectureId()).orElseThrow();
        Student student = studentRepository.findByEmail(email).orElseThrow();
        QuestionLecture questionLecture = QuestionLecture
                .builder()
                .title(questionLecturePostVM.title())
                .description(questionLecturePostVM.description())
                .lecture(lecture)
                .student(student)
                .build();
        questionLecture.setCreatedAt(LocalDateTime.now());
        questionLecture.setUpdatedAt(LocalDateTime.now());

        QuestionLecture savedQuestionLecture = questionLectureRepo.saveAndFlush(questionLecture);
        return QuestionLectureVM.fromModel(savedQuestionLecture);
    }

    @Override
    public List<QuestionLectureGetVM> getByLectureId(Long lectureId) {

        List<QuestionLecture> questionLectures = questionLectureRepo.getByLectureId(lectureId);

        List<QuestionLectureGetVM> questionLectureGetVMS = questionLectures.stream().map(questionLecture -> {

            List<AnswerLecture> answerLectures = new ArrayList<>();


            List<UserAnswer> userAnswers = userAnswerRepo.getByQuestionLectureId(questionLecture.getId());
            for (UserAnswer userAnswer : userAnswers) {
                answerLectures.add(AnswerLecture.fromModelUser(userAnswer));
            }
            List<StudentAnswer> studentAnswers = studentAnswerRepo.getByQuestionLectureId(questionLecture.getId());
            for (StudentAnswer studentAnswer : studentAnswers) {
                answerLectures.add(AnswerLecture.fromModelStudent(studentAnswer));
            }


            return QuestionLectureGetVM.fromModel(questionLecture, answerLectures);
        }).toList();
        return questionLectureGetVMS;
    }
}
