package com.backend.elearning.domain.question;

import com.backend.elearning.domain.answer.Answer;
import com.backend.elearning.domain.answer.AnswerRepository;
import com.backend.elearning.domain.answer.AnswerVM;
import com.backend.elearning.domain.quiz.Quiz;
import com.backend.elearning.domain.quiz.QuizRepository;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public QuestionVM create(QuestionPostVM questionVM) {
        Quiz quiz = quizRepository.findById(questionVM.quizId()).orElseThrow(
                () -> new NotFoundException(Constants.ERROR_CODE.QUIZ_NOT_FOUND, questionVM.quizId())
        );
        Question question = Question.builder()
                .title(questionVM.title())
                .quiz(quiz)
                .build();
        Question savedQuestion = questionRepository.saveAndFlush(question);
        List<AnswerVM> answerVMS = new ArrayList<>();
        if (!questionVM.answers().isEmpty()) {
            questionVM.answers().forEach(answerVM -> {
                Answer answer = Answer.builder()
                        .answerText(answerVM.answerText())
                        .correct(answerVM.correct())
                        .reason(answerVM.reason())
                        .question(savedQuestion)
                        .build();
                Answer savedAnswer = answerRepository.saveAndFlush(answer);
                answerVMS.add(AnswerVM.fromModel(savedAnswer));
            });
        }
        return QuestionVM.fromModel(savedQuestion, answerVMS);
    }

    @Override
    public QuestionVM update(QuestionPostVM questionPostVM, Long questionId) {
        Question question = questionRepository.findByIdReturnAnswers(questionId).orElseThrow(
                () -> new NotFoundException(Constants.ERROR_CODE.QUESTION_NOT_FOUND, questionId)
        );
        question.setTitle(questionPostVM.title());
        if (question.getQuiz().getId() != questionPostVM.quizId()) {
            throw new BadRequestException("");
        }
        List<AnswerVM> answerVMS = new ArrayList<>();
        if (!questionPostVM.answers().isEmpty()) {
            questionPostVM.answers().forEach(answerVM -> {
                Answer answer;
                if (answerVM.id() != null) {
                    // Todo: Error code of answer
                    answer =  answerRepository.findById(answerVM.id()).orElseThrow();
                    answer.setAnswerText(answerVM.answerText());
                    answer.setCorrect(answerVM.correct());
                    answer.setReason(answerVM.reason());
                } else {
                    answer = Answer.builder()
                            .answerText(answerVM.answerText())
                            .reason(answerVM.reason())
                            .question(question)
                            .build();
                }
                Answer savedAnswer = answerRepository.saveAndFlush(answer);
                answerVMS.add(AnswerVM.fromModel(savedAnswer));
            });
        }
        return QuestionVM.fromModel(question, answerVMS);
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

    @Override
    public QuestionVM getById(Long questionId) {
        Question question = questionRepository.findByIdReturnAnswers(questionId).orElseThrow(
                () -> new NotFoundException(Constants.ERROR_CODE.QUESTION_NOT_FOUND, questionId)
        );
        List<AnswerVM> answerVMS=  question.getAnswers().stream().map(AnswerVM::fromModel).toList();
        return QuestionVM.fromModel(question, answerVMS);
    }
}
