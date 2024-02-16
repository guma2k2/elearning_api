package com.backend.elearning.domain.quiz;

import com.backend.elearning.domain.common.Curriculum;
import com.backend.elearning.domain.common.ECurriculumType;
import com.backend.elearning.domain.question.QuestionVM;

import java.util.ArrayList;
import java.util.List;

public class QuizVM extends Curriculum {

    private String description;

    private List<QuestionVM> questions = new ArrayList<>();



    public QuizVM(List<QuestionVM> questions) {
        this.questions = questions;
    }

    public QuizVM(Long id, String title, int number, List<QuestionVM> questions) {
        super(id, title, number);
        this.questions = questions;
    }

    public QuizVM(Long id, String title, float number, ECurriculumType type, String description, List<QuestionVM> questions) {
        super(id, title, number, type);
        this.description = description;
        this.questions = questions;
    }
    public QuizVM(Quiz quiz) {
        super(quiz.getId(), quiz.getTitle(), quiz.getNumber(), ECurriculumType.quiz);
        this.description = quiz.getDescription();
        this.questions = new ArrayList<>();
    }

    public QuizVM(Long id, String title, float number, ECurriculumType type, List<QuestionVM> questions) {
        super(id, title, number, type);
        this.questions = questions;
    }

    public QuizVM() {


    }

    public List<QuestionVM> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionVM> questions) {
        this.questions = questions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
