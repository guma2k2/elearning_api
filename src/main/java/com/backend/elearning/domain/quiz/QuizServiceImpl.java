package com.backend.elearning.domain.quiz;

import com.backend.elearning.domain.section.Section;
import com.backend.elearning.domain.section.SectionRepository;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QuizServiceImpl implements QuizService{
    private final QuizRepository quizRepository;
    private final SectionRepository sectionRepository;

    public QuizServiceImpl(QuizRepository quizRepository, SectionRepository sectionRepository) {
        this.quizRepository = quizRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public QuizVM create(QuizPostVM quizPostVM) {
        Section section = sectionRepository.findById(quizPostVM.sectionId()).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.LECTURE_NOT_FOUND, quizPostVM.sectionId()))  ;
        Quiz quiz = Quiz.builder()
                .title(quizPostVM.title())
                .number(quizPostVM.number())
                .description(quizPostVM.description())
                .section(section)
                .build();
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUpdatedAt(LocalDateTime.now());
        Quiz savedQuiz = quizRepository.save(quiz);
        return new QuizVM(savedQuiz);
    }

    @Override
    public QuizVM update(QuizPostVM quizPutVM, Long quizId) {
        Quiz quiz = quizRepository.findByIdReturnSection(quizId).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.LECTURE_NOT_FOUND, quizId));
        if (quiz.getSection().getId() != quizPutVM.sectionId()) {
            throw new BadRequestException(Constants.ERROR_CODE.LECTURE_SECTION_NOT_SAME, quizPutVM.sectionId());
        }
        quiz.setTitle(quizPutVM.title());
        quiz.setDescription(quizPutVM.description());
        quiz.setNumber(quizPutVM.number());
        quiz.setUpdatedAt(LocalDateTime.now());
        Quiz savedQuiz = quizRepository.saveAndFlush(quiz);
        return new QuizVM(savedQuiz);
    }

    @Override
    public void delete(Long quizId) {
        Quiz quiz = quizRepository.findByIdReturnSection(quizId).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.LECTURE_NOT_FOUND, quizId));
        boolean canDelete = quiz.getQuestions().isEmpty();
        if (!canDelete) {
            throw new BadRequestException("");
        }
        quizRepository.delete(quiz);
    }
}
