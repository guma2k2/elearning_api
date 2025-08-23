package com.backend.elearning.domain.questionLecture;

import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.lecture.LectureRepository;
import com.backend.elearning.domain.questionLecture.studentAnswer.StudentAnswer;
import com.backend.elearning.domain.questionLecture.studentAnswer.StudentAnswerRepo;
import com.backend.elearning.domain.questionLecture.userAnswer.UserAnswer;
import com.backend.elearning.domain.questionLecture.userAnswer.UserAnswerRepo;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
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
        log.info("received questionLecturePostVM: {}", questionLecturePostVM);
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

        QuestionLecture savedQuestionLecture = questionLectureRepo.saveAndFlush(questionLecture);
        return QuestionLectureVM.fromModel(savedQuestionLecture);
    }

    @Override
    public QuestionLectureVM update(QuestionLecturePostVM questionLecturePostVM, Long questionLectureId) {
        log.info("update questionLecturePostVM: {} with id: {}", questionLecturePostVM, questionLectureId);
        QuestionLecture questionLecture = questionLectureRepo.findById(questionLectureId).orElseThrow();
        questionLecture.setTitle(questionLecturePostVM.title());
        questionLecture.setDescription(questionLecturePostVM.description());
        QuestionLecture savedQuestionLecture = questionLectureRepo.saveAndFlush(questionLecture);

        return QuestionLectureVM.fromModel(savedQuestionLecture);
    }

    @Override
    public void delete(Long questionLectureId) {
        log.info("deleting question with id: {}", questionLectureId);
        QuestionLecture questionLecture = questionLectureRepo.findById(questionLectureId).orElseThrow();
        if (questionLecture.getStudentAnswers().size() > 0 || questionLecture.getUserAnswers().size() > 0) {
            throw new BadRequestException("Câu hỏi này đã có câu trả lời");
        }
        questionLectureRepo.deleteById(questionLectureId);
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
            answerLectures.sort(Comparator.comparing(AnswerLecture::createdAt));
            return QuestionLectureGetVM.fromModel(questionLecture, answerLectures);
        }).toList();
        return questionLectureGetVMS;
    }

    @Override
    public List<QuestionLectureGetVM> getByCourse(Long courseId) {
        List<QuestionLecture> questionLectures = questionLectureRepo.getByCourse(courseId);

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
            answerLectures.sort(Comparator.comparing(AnswerLecture::createdAt));
            return QuestionLectureGetVM.fromModel(questionLecture, answerLectures);
        }).toList();
        return questionLectureGetVMS;
    }

    @Override
    public List<QuestionLectureGetVM> getBySection(Long sectionId) {
        List<QuestionLecture> questionLectures = questionLectureRepo.getBySection(sectionId);

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
            answerLectures.sort(Comparator.comparing(AnswerLecture::createdAt));
            return QuestionLectureGetVM.fromModel(questionLecture, answerLectures);
        }).toList();
        return questionLectureGetVMS;
    }
}
