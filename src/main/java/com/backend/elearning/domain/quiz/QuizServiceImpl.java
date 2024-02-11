package com.backend.elearning.domain.quiz;

import com.backend.elearning.domain.section.Section;
import com.backend.elearning.domain.section.SectionRepository;
import org.springframework.stereotype.Service;

@Service
public class QuizServiceImpl implements QuizService{
    private final QuizRepository quizRepository;
    private final SectionRepository sectionRepository;

    public QuizServiceImpl(QuizRepository quizRepository, SectionRepository sectionRepository) {
        this.quizRepository = quizRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public void create(QuizPostVM quizPostVM) {
        Section section = sectionRepository.findById(quizPostVM.sectionId()).orElseThrow()  ;
        Quiz quiz = Quiz.builder()
                .title(quizPostVM.title())
                .number(quizPostVM.number())
                .description(quizPostVM.description())
                .section(section)
                .build();
        quizRepository.save(quiz);
    }
}
