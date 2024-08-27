package com.backend.elearning.service;

import com.backend.elearning.domain.lecture.LectureServiceImpl;
import com.backend.elearning.domain.question.Question;
import com.backend.elearning.domain.quiz.*;
import com.backend.elearning.domain.section.Section;
import com.backend.elearning.domain.section.SectionRepository;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private SectionRepository sectionRepository;

    private QuizService quizService;
    @BeforeEach
    void beforeEach() {
        quizService = new QuizServiceImpl(quizRepository, sectionRepository);
    }

    @Test
    void shouldCreateQuizSuccessfully() {
        // Given
        QuizPostVM quizPostVM = new QuizPostVM(1L, "Quiz Title", 1.0f, "Description", 100L);
        Section section = new Section();
        section.setId(quizPostVM.sectionId());

        when(sectionRepository.findById(quizPostVM.sectionId())).thenReturn(Optional.of(section));
        when(quizRepository.save(Mockito.any(Quiz.class))).thenAnswer(invocation -> {
            Quiz quiz = invocation.getArgument(0);
            quiz.setId(1L);
            return quiz;
        });

        // When
        QuizVM result = quizService.create(quizPostVM);

        // Then
        assertNotNull(result);
        assertEquals(quizPostVM.title(), result.getTitle());
        verify(quizRepository).save(Mockito.any(Quiz.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSectionNotFound() {
        // Given
        QuizPostVM quizPostVM = new QuizPostVM(1L, "Quiz Title", 1.0f, "Description", 100L);

        when(sectionRepository.findById(quizPostVM.sectionId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> quizService.create(quizPostVM));
    }

    @Test
    void shouldUpdateQuizSuccessfully() {
        // Given
        QuizPostVM quizPutVM = new QuizPostVM(1L, "Updated Title", 1.5f, "Updated Description", 100L);
        Section section = new Section();
        section.setId(quizPutVM.sectionId());
        Quiz existingQuiz = new Quiz();
        existingQuiz.setId(1L);
        existingQuiz.setSection(section);

        when(quizRepository.findByIdReturnSection(1L)).thenReturn(Optional.of(existingQuiz));
        when(quizRepository.saveAndFlush(Mockito.any(Quiz.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        QuizVM result = quizService.update(quizPutVM, 1L);

        // Then
        assertNotNull(result);
        assertEquals(quizPutVM.title(), result.getTitle());
        assertEquals(quizPutVM.number(), result.getNumber());
        verify(quizRepository).saveAndFlush(Mockito.any(Quiz.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenQuizNotFoundForUpdate() {
        // Given
        QuizPostVM quizPutVM = new QuizPostVM(1L, "Updated Title", 1.5f, "Updated Description", 100L);

        when(quizRepository.findByIdReturnSection(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> quizService.update(quizPutVM, 1L));
    }

    @Test
    void shouldThrowBadRequestExceptionWhenSectionIdMismatchForUpdate() {
        // Given
        QuizPostVM quizPutVM = new QuizPostVM(1L, "Updated Title", 1.5f, "Updated Description", 101L);
        Section section = new Section();
        section.setId(100L);
        Quiz existingQuiz = new Quiz();
        existingQuiz.setId(1L);
        existingQuiz.setSection(section);

        when(quizRepository.findByIdReturnSection(1L)).thenReturn(Optional.of(existingQuiz));

        // When & Then
        assertThrows(BadRequestException.class, () -> quizService.update(quizPutVM, 1L));
    }

    @Test
    void shouldDeleteQuizSuccessfully() {
        // Given
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        Section section = new Section();
        section.setId(100L);
        quiz.setSection(section);

        when(quizRepository.findByIdReturnSection(1L)).thenReturn(Optional.of(quiz));

        // When
        assertDoesNotThrow(() -> quizService.delete(1L));

        // Then
        verify(quizRepository).delete(quiz);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenQuizNotFoundForDelete() {
        // Given
        when(quizRepository.findByIdReturnSection(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> quizService.delete(1L));
    }

    @Test
    void shouldThrowBadRequestExceptionWhenQuizHasQuestionsForDelete() {
        // Given
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        Question question = new Question();
        quiz.setQuestions(Collections.singletonList(question));

        when(quizRepository.findByIdReturnSection(1L)).thenReturn(Optional.of(quiz));

        // When & Then
        assertThrows(BadRequestException.class, () -> quizService.delete(1L));
    }
}
