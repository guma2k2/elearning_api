package com.backend.elearning.domain.quiz;

import com.backend.elearning.domain.common.Curriculum;
import com.backend.elearning.domain.common.ECurriculumType;
import com.backend.elearning.domain.question.QuestionVM;
import com.backend.elearning.utils.DateTimeUtils;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuizVM extends Curriculum {

    private String description;

    private List<QuestionVM> questions = new ArrayList<>();

    private boolean finished;

    public QuizVM(List<QuestionVM> questions) {
        this.questions = questions;
    }

    public QuizVM(Quiz quiz) {
        super(quiz.getId(), quiz.getTitle(), quiz.getNumber(), ECurriculumType.quiz, quiz.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToMonthYearText(quiz.getUpdatedAt()) : "");
        this.description = quiz.getDescription();
        this.questions = new ArrayList<>();
    }
    public QuizVM(Quiz quiz, boolean finished) {
        super(quiz.getId(), quiz.getTitle(), quiz.getNumber(), ECurriculumType.quiz, quiz.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToMonthYearText(quiz.getUpdatedAt()) : "");
        this.description = quiz.getDescription();
        this.questions = new ArrayList<>();
        this.finished = finished;
    }


}
