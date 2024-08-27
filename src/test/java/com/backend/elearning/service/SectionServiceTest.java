package com.backend.elearning.service;

import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.learning.learningLecture.LearningLectureRepository;
import com.backend.elearning.domain.learning.learningQuiz.LearningQuizRepository;
import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.question.QuestionService;
import com.backend.elearning.domain.quiz.Quiz;
import com.backend.elearning.domain.section.*;
import com.backend.elearning.domain.user.UserServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SectionServiceTest {

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private QuestionService questionService;

    @Mock
    private LearningLectureRepository learningLectureRepository;

    @Mock
    private LearningQuizRepository learningQuizRepository;

    private SectionService sectionService;

    @BeforeEach
    void beforeEach() {
        sectionService = new SectionServiceImpl(sectionRepository, courseRepository, questionService, learningLectureRepository, learningQuizRepository);
    }
    @Test
    void shouldThrowNotFoundException_whenCourseNotFound() {
        // Given
        SectionPostVM sectionPostVM = new SectionPostVM(1L, "Section 1", 1.0f, "Objective", 999L);
        when(courseRepository.findById(sectionPostVM.courseId())).thenReturn(Optional.empty());

        // When and then
        assertThrows(NotFoundException.class, () -> sectionService.create(sectionPostVM));
    }


    @Test
    void shouldCreateSectionSuccessfully_whenValidSectionPostVMProvided() {
        // Given: A valid SectionPostVM with a courseId that exists in the repository.
        Course course = new Course();
        SectionPostVM sectionPostVM = new SectionPostVM(1L, "Section 1", 1.0f, "Objective", 1L);
        when(courseRepository.findById(sectionPostVM.courseId())).thenReturn(Optional.of(course));
        when(sectionRepository.save(any(Section.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When: create is called with the SectionPostVM.
        SectionVM sectionVM = sectionService.create(sectionPostVM);

        // Then: A new Section is created and saved in the repository, and a SectionVM is returned.
        assertNotNull(sectionVM);
        assertEquals(sectionPostVM.title(), sectionVM.title());
        verify(sectionRepository).save(any(Section.class));
    }


    @Test
    void shouldThrowNotFoundException_whenSectionDoesNotExist() {
        // Given: A SectionPostVM and a sectionId that does not exist in the repository.
        SectionPostVM sectionPutVM = new SectionPostVM(1L, "Updated Title", 2.0f, "Updated Objective", 1L);
        when(sectionRepository.findByIdReturnCourse(sectionPutVM.id())).thenReturn(Optional.empty());

        // When: update is called with the SectionPostVM and the sectionId.
        // Then: A NotFoundException is thrown with the error code SECTION_NOT_FOUND.
        assertThrows(NotFoundException.class, () -> sectionService.update(sectionPutVM, sectionPutVM.id()));
    }

    @Test
    void shouldThrowBadRequestException_whenCourseIdMismatch() {
        // Given: A SectionPostVM with a different courseId than the existing section's courseId.
        Section existingSection = new Section();
        existingSection.setCourse(new Course());
        existingSection.getCourse().setId(2L);
        SectionPostVM sectionPutVM = new SectionPostVM(1L, "Updated Title", 2.0f, "Updated Objective", 1L);
        when(sectionRepository.findByIdReturnCourse(sectionPutVM.id())).thenReturn(Optional.of(existingSection));

        // When: update is called with the SectionPostVM.
        // Then: A BadRequestException is thrown with an appropriate error message.
        assertThrows(BadRequestException.class, () -> sectionService.update(sectionPutVM, sectionPutVM.id()));
    }

    @Test
    void shouldUpdateSectionSuccessfully_whenValidSectionPutVMProvided() {
        // Given: A valid SectionPostVM with matching courseId and an existing section.
        Section existingSection = new Section();
        existingSection.setCourse(new Course());
        existingSection.getCourse().setId(1L);
        SectionPostVM sectionPutVM = new SectionPostVM(1L, "Updated Title", 2.0f, "Updated Objective", 1L);
        when(sectionRepository.findByIdReturnCourse(sectionPutVM.id())).thenReturn(Optional.of(existingSection));
        when(sectionRepository.saveAndFlush(any(Section.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When: update is called with the SectionPostVM and the sectionId.
        SectionVM sectionVM = sectionService.update(sectionPutVM, sectionPutVM.id());

        // Then: The section is updated with the new details and saved in the repository.
        assertNotNull(sectionVM);
        assertEquals(sectionPutVM.title(), sectionVM.title());
        verify(sectionRepository).saveAndFlush(any(Section.class));
    }

    @Test
    void shouldThrowNotFoundException_whenDeleteSectionDoesNotExist() {
        // Given: A sectionId that does not exist in the repository.
        when(sectionRepository.findByIdLectures(anyLong())).thenReturn(Optional.empty());

        // When: delete is called with the sectionId.
        // Then: A NotFoundException is thrown with the error code SECTION_NOT_FOUND.
        assertThrows(NotFoundException.class, () -> sectionService.delete(1L));
    }

    @Test
    void shouldThrowBadRequestException_whenDeleteSectionHasLecturesOrQuizzes() {
        // Given: A section with existing lectures or quizzes.
        Section section = new Section();
        section.setLectures(Collections.singletonList(new Lecture()));
        section.setQuizzes(Collections.singletonList(new Quiz()));
        when(sectionRepository.findByIdLectures(anyLong())).thenReturn(Optional.of(section));
        when(sectionRepository.findByIdQuizzes(any(Section.class))).thenReturn(Optional.of(section));

        // When: delete is called with the sectionId.
        // Then: A BadRequestException is thrown with an appropriate error message.
        assertThrows(BadRequestException.class, () -> sectionService.delete(1L));
    }

    @Test
    void shouldDeleteSectionSuccessfully_whenDeleteWithNoLecturesOrQuizzes() {
        // Given: A section with no lectures or quizzes.
        Section section = new Section();
        section.setLectures(Collections.emptyList());
        section.setQuizzes(Collections.emptyList());
        when(sectionRepository.findByIdLectures(anyLong())).thenReturn(Optional.of(section));
        when(sectionRepository.findByIdQuizzes(any(Section.class))).thenReturn(Optional.of(section));

        // When: delete is called with the sectionId.
        sectionService.delete(1L);

        // Then: The section is deleted from the repository.
        verify(sectionRepository).delete(section);
    }
}
