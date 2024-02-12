package com.backend.elearning.domain.question;

import com.backend.elearning.domain.answer.Answer;
import com.backend.elearning.domain.answer.AnswerRepository;
import com.backend.elearning.domain.answer.AnswerVM;
import com.backend.elearning.domain.quiz.Quiz;
import com.backend.elearning.domain.quiz.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService{
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuizRepository quizRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, AnswerRepository answerRepository, QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public void create(QuestionPostVM questionVM) {
        Quiz quiz = quizRepository.findById(questionVM.quizId()).orElseThrow();
        Question question = Question.builder()
                .title(questionVM.title())
                .quiz(quiz)
                .build();
        Question savedQuestion = questionRepository.save(question);
        if (!questionVM.answers().isEmpty()) {
            questionVM.answers().forEach(answerVM -> {
                Answer answer = Answer.builder()
                        .answerText(answerVM.answerText())
                        .reason(answerVM.reason())
                        .question(savedQuestion)
                        .build();
                answerRepository.save(answer);
            });
        }
    }

    @Override
    public List<QuestionVM> getByQuizId(Long quizId) {
        List<Question> questions = questionRepository.findByQuiz(quizId);
        List<QuestionVM> questionVMS = questions.stream().map(question -> {
            List<AnswerVM> answers = question.getAnswers().stream().map(AnswerVM::fromModel).toList();
            return QuestionVM.fromModel(question, answers);
        }).toList();
        return questionVMS;
    }
}
