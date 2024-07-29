package com.backend.elearning.service;

import com.backend.elearning.domain.learning.learningLecture.LearningLecture;
import com.backend.elearning.domain.learning.learningLecture.LearningLecturePostVM;
import com.backend.elearning.domain.learning.learningLecture.LearningLectureRepository;
import com.backend.elearning.domain.learning.learningLecture.LearningLectureService;
import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.lecture.LectureRepository;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LearningLectureServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private LearningLectureRepository learningLectureRepository;

    private LearningLectureService learningLectureService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void beforeEach() {
        learningLectureService = new LearningLectureService(learningLectureRepository, lectureRepository, studentRepository );
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testCreateLearningLecture_Success() {
        // Given
        String email = "test@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);


        LearningLecturePostVM learningLecturePostVM = new LearningLecturePostVM(1L, 120, true);
        Lecture lecture = new Lecture(); // Replace with actual creation logic
        Student student = new Student(); // Replace with actual creation logic

        when(lectureRepository.findById(learningLecturePostVM.lectureId())).thenReturn(Optional.of(lecture));
        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(student));

        // When
        learningLectureService.create(learningLecturePostVM);

        // Then
        verify(learningLectureRepository).save(Mockito.any(LearningLecture.class));
    }

    @Test
    public void testCreateLearningLecture_LectureNotFound() {
        String email = "test@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        // Given
        LearningLecturePostVM learningLecturePostVM = new LearningLecturePostVM(1L, 120, true);
        when(lectureRepository.findById(learningLecturePostVM.lectureId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            learningLectureService.create(learningLecturePostVM);
        });
    }

    @Test
    public void testCreateLearningLecture_StudentNotFound() {
        // Given
        String email = "test@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);

        LearningLecturePostVM learningLecturePostVM = new LearningLecturePostVM(1L, 120, true);
        Lecture lecture = new Lecture(); // Replace with actual creation logic

        when(lectureRepository.findById(learningLecturePostVM.lectureId())).thenReturn(Optional.of(lecture));
        when(studentRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            learningLectureService.create(learningLecturePostVM);
        });
    }
}
