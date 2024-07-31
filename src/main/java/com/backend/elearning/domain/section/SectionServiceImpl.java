package com.backend.elearning.domain.section;

import com.backend.elearning.domain.common.Curriculum;
import com.backend.elearning.domain.common.ECurriculumType;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.learning.learningLecture.LearningLecture;
import com.backend.elearning.domain.learning.learningLecture.LearningLectureRepository;
import com.backend.elearning.domain.learning.learningQuiz.LearningQuiz;
import com.backend.elearning.domain.learning.learningQuiz.LearningQuizRepository;
import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.lecture.LectureVm;
import com.backend.elearning.domain.question.QuestionService;
import com.backend.elearning.domain.question.QuestionVM;
import com.backend.elearning.domain.quiz.Quiz;
import com.backend.elearning.domain.quiz.QuizVM;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import com.backend.elearning.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class SectionServiceImpl implements SectionService{
    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;
    private final QuestionService questionService;

    private final LearningLectureRepository learningLectureRepository;
    private final LearningQuizRepository learningQuizRepository;
    public SectionServiceImpl(SectionRepository sectionRepository, CourseRepository courseRepository, QuestionService questionService, LearningLectureRepository learningLectureRepository, LearningQuizRepository learningQuizRepository) {
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
        this.questionService = questionService;
        this.learningLectureRepository = learningLectureRepository;
        this.learningQuizRepository = learningQuizRepository;
    }

    @Override
    public SectionVM create(SectionPostVM sectionPostVM) {
        Course course = courseRepository.findById(sectionPostVM.courseId()).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.COURSE_NOT_FOUND, sectionPostVM.courseId()));
        Section section = Section.builder()
                .title(sectionPostVM.title())
                .number(sectionPostVM.number())
                .objective(sectionPostVM.objective())
                .course(course)
                .build();
        return SectionVM.fromModel(sectionRepository.save(section), new ArrayList<>());
    }

    @Override
    public SectionVM getById(Long sectionId, String email) {
        // Hibernate throws MultipleBagFetchException - cannot simultaneously fetch multiple bags
        // Vlad Mihalcea in his answer!
        // https://stackoverflow.com/questions/4334970/hibernate-throws-multiplebagfetchexception-cannot-simultaneously-fetch-multipl/51055523#51055523
        Section section = sectionRepository.findByIdLectures(sectionId).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.SECTION_NOT_FOUND, sectionId));
        section = sectionRepository.findByIdQuizzes(section).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.SECTION_NOT_FOUND, sectionId));
        List<Curriculum> curriculumList = getBySectionAndEmail(section, email);
        return SectionVM.fromModel(section, curriculumList);
    }

    @Override
    public SectionVM update(SectionPostVM sectionPutVM, Long sectionId) {
        Section section = sectionRepository.findByIdReturnCourse(sectionId).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.SECTION_NOT_FOUND, sectionId));
        if (sectionPutVM.courseId() != section.getCourse().getId()) {
            throw new BadRequestException("");
        }
        section.setTitle(sectionPutVM.title());
        section.setNumber(sectionPutVM.number());
        section.setObjective(sectionPutVM.objective());
        sectionRepository.saveAndFlush(section);
        return SectionVM.fromModel(section, new ArrayList<>());
    }

    @Override
    @Transactional
    public void delete(Long sectionId) {
        Section section = sectionRepository.findByIdLectures(sectionId).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.SECTION_NOT_FOUND, sectionId));
        section = sectionRepository.findByIdQuizzes(section).orElseThrow();
        boolean canDelete = section.getLectures().isEmpty() && section.getQuizzes().isEmpty();
        if (!canDelete) {
            throw new BadRequestException("");
        }
        sectionRepository.delete(section);
    }

    @Override
    public List<Section> findByCourseId(Long courseId) {
        List<Section> sections = sectionRepository.findSectionWithLecturesByCourseId(courseId);
        sections = sections.stream().map((section -> sectionRepository.findByIdQuizzes(section).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.SECTION_NOT_FOUND, section.getId())))).toList();
        return sections;
    }

    private String convertSecondToFormattedDuration (int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private List<Curriculum> getBySectionAndEmail(Section section, String email) {
        List<Curriculum> curriculumList = new ArrayList<>();

        List<Lecture> lectures = section.getLectures();
        for (Lecture lecture : lectures) {
            LectureVm lectureVm = new LectureVm();
            lectureVm.setId(lecture.getId());
            lectureVm.setType(ECurriculumType.lecture);
            lectureVm.setTitle(lecture.getTitle());
            lectureVm.setVideoId(lecture.getVideoId());
            lectureVm.setLectureDetails(lecture.getLectureDetails());
            lectureVm.setNumber(lecture.getNumber());
            String formatedUpdatedAt = lecture.getUpdatedAt() != null ? DateTimeUtils.convertLocalDateTimeToMonthYearText(lecture.getUpdatedAt()) : "";
            lectureVm.setUpdatedAt(formatedUpdatedAt);
            String formattedDuration = convertSecondToFormattedDuration(lecture.getDuration());
            if (email != null) {
                Optional<LearningLecture> learningLectureOptional = learningLectureRepository.findByEmailAndLectureId(email, lecture.getId());
                if (learningLectureOptional.isPresent())    {
                    LearningLecture learningLecture = learningLectureOptional.get();
                    lectureVm.setFinished(learningLecture.isFinished());
                    lectureVm.setWatchingSecond(learningLecture.getWatchingSecond());
                } else {
                    lectureVm.setWatchingSecond(0);
                    lectureVm.setFinished(false);
                }
            }
            lectureVm.setDuration(lecture.getDuration());
            lectureVm.setFormattedDuration(formattedDuration);
            curriculumList.add(lectureVm);
        }

        List<Quiz> quizzes = section.getQuizzes();
        for (Quiz quiz : quizzes) {
            QuizVM quizVM = new QuizVM();
            quizVM.setId(quiz.getId());
            quizVM.setTitle(quiz.getTitle());
            quizVM.setNumber(quiz.getNumber());
            quizVM.setDescription(quiz.getDescription());
            quizVM.setType(ECurriculumType.quiz);
            List<QuestionVM> questionVMS = questionService.getByQuizId(quiz.getId());
            quizVM.setQuestions(questionVMS);
            String formatedUpdatedAt = quiz.getUpdatedAt() != null ? DateTimeUtils.convertLocalDateTimeToMonthYearText(quiz.getUpdatedAt()) : "";
            quizVM.setUpdatedAt(formatedUpdatedAt);
            if (email != null) {
                Optional<LearningQuiz> learningQuizOptional = learningQuizRepository.findByEmailAndQuizId(email, quiz.getId());
                if (learningQuizOptional.isPresent())    {
                    LearningQuiz learningQuiz = learningQuizOptional.get();
                    quizVM.setFinished(learningQuiz.isFinished());
                } else {
                    quizVM.setFinished(false);
                }
            }
            curriculumList.add(quizVM);
        }
        curriculumList.sort(Comparator.comparing(Curriculum::getNumber));
        return curriculumList;
    }
}
