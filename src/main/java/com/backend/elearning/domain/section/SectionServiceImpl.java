package com.backend.elearning.domain.section;

import com.backend.elearning.domain.common.Curriculum;
import com.backend.elearning.domain.common.ECurriculumType;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.lecture.LectureVm;
import com.backend.elearning.domain.question.QuestionService;
import com.backend.elearning.domain.question.QuestionVM;
import com.backend.elearning.domain.quiz.Quiz;
import com.backend.elearning.domain.quiz.QuizVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class SectionServiceImpl implements SectionService{
    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;
    private final QuestionService questionService;

    public SectionServiceImpl(SectionRepository sectionRepository, CourseRepository courseRepository, QuestionService questionService) {
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
        this.questionService = questionService;
    }

    @Override
    public SectionVM create(SectionPostVM sectionPostVM) {
        Course course = courseRepository.findById(sectionPostVM.courseId()).orElseThrow();
        Section section = Section.builder()
                .title(sectionPostVM.title())
                .number(sectionPostVM.number())
                .objective(sectionPostVM.objective())
                .course(course)
                .build();
        return SectionVM.fromModel(sectionRepository.save(section), new ArrayList<>());
    }

    @Override
    public SectionVM getById(Long sectionId) {
        // Hibernate throws MultipleBagFetchException - cannot simultaneously fetch multiple bags
        // Vlad Mihalcea in his answer!
        // https://stackoverflow.com/questions/4334970/hibernate-throws-multiplebagfetchexception-cannot-simultaneously-fetch-multipl/51055523#51055523
        Section section = sectionRepository.findByIdLecturesQuizzes(sectionId).orElseThrow();
        section = sectionRepository.findByIdQuizzes(section).orElseThrow();
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
            lectureVm.setDuration(lecture.getDuration());
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
            // Todo get all question by quizId
            List<QuestionVM> questionVMS = questionService.getByQuizId(quiz.getId());
            quizVM.setQuestions(questionVMS);
            curriculumList.add(quizVM);
        }
        curriculumList.sort(Comparator.comparing(Curriculum::getNumber));
        return SectionVM.fromModel(section, curriculumList);
    }
}
