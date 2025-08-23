package com.backend.elearning.domain.questionLecture.userAnswer;


import com.backend.elearning.domain.questionLecture.AnswerLecture;
import com.backend.elearning.domain.questionLecture.QuestionLecture;
import com.backend.elearning.domain.questionLecture.QuestionLectureRepo;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserAnswerServiceImpl implements UserAnswerService {

    private final UserAnswerRepo userAnswerRepo;
    private final QuestionLectureRepo questionLectureRepo;
    private final UserRepository userRepository;

    public UserAnswerServiceImpl(UserAnswerRepo userAnswerRepo, QuestionLectureRepo questionLectureRepo, UserRepository userRepository) {
        this.userAnswerRepo = userAnswerRepo;
        this.questionLectureRepo = questionLectureRepo;
        this.userRepository = userRepository;
    }

    @Override
    public AnswerLecture create(UserAnswerPostVM userAnswerPostVM) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        QuestionLecture questionLecture = questionLectureRepo.findById(userAnswerPostVM.questionLectureId()).orElseThrow();
        User user = userRepository.findByEmail(email).orElseThrow();
        UserAnswer userAnswer = UserAnswer
                .builder()
                .content(userAnswerPostVM.content())
                .user(user)
                .questionLecture(questionLecture)
                .build();

        UserAnswer savedUserAnswer = userAnswerRepo.saveAndFlush(userAnswer);
        return AnswerLecture.fromModelUser(savedUserAnswer);
    }

    @Override
    public AnswerLecture update(UserAnswerPostVM userAnswerPostVM, Long userAnswerId) {
        UserAnswer userAnswer = userAnswerRepo.findById(userAnswerId).orElseThrow();
        userAnswer.setContent(userAnswerPostVM.content());
        UserAnswer savedUserAnswer = userAnswerRepo.saveAndFlush(userAnswer);
        return AnswerLecture.fromModelUser(savedUserAnswer);
    }

    @Override
    public void delete(Long answerLectureId) {
        userAnswerRepo.deleteById(answerLectureId);
    }
}
