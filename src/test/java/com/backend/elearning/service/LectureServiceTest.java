package com.backend.elearning.service;

import com.backend.elearning.domain.lecture.*;
import com.backend.elearning.domain.section.Section;
import com.backend.elearning.domain.section.SectionRepository;
import com.backend.elearning.domain.section.SectionServiceImpl;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private SectionRepository sectionRepository;


    private LectureService lectureService;

    @BeforeEach
    void beforeEach() {
        lectureService = new LectureServiceImpl(lectureRepository, sectionRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenSectionNotFound() {
        // Given: A LecturePostVM with a sectionId that does not exist in the repository.
        LecturePostVM lecturePostVM = new LecturePostVM(1L, "Lecture Title", 1.0f, "Lecture Details", 60, "VideoId", 999L);
        when(sectionRepository.findById(lecturePostVM.sectionId())).thenReturn(Optional.empty());

        // When: create is called with the LecturePostVM.
        // Then: A NotFoundException is thrown with the error code SECTION_NOT_FOUND.
        assertThrows(NotFoundException.class, () -> lectureService.create(lecturePostVM));
    }

    @Test
    void shouldCreateLectureSuccessfully_whenValidLecturePostVMProvided() {
        // Given: A valid LecturePostVM with an existing section.
        Section section = new Section();
        LecturePostVM lecturePostVM = new LecturePostVM(1L, "Lecture Title", 1.0f, "Lecture Details", 60, "VideoId", 1L);
        when(sectionRepository.findById(lecturePostVM.sectionId())).thenReturn(Optional.of(section));
        when(lectureRepository.save(Mockito.any(Lecture.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When: create is called with the LecturePostVM.
        LectureVm lectureVm = lectureService.create(lecturePostVM);

        // Then: A new Lecture is created and saved in the repository, and a LectureVm is returned.
        assertNotNull(lectureVm);
        assertEquals(lecturePostVM.title(), lectureVm.getTitle());
        verify(lectureRepository).save(Mockito.any(Lecture.class));
    }

    @Test
    void shouldThrowNotFoundException_whenLectureNotFound() {
        // Given: A LecturePostVM and a lectureId that does not exist in the repository.
        LecturePostVM lecturePutVM = new LecturePostVM(1L, "Updated Title", 2.0f, "Updated Details", 75, "NewVideoId", 1L);
        when(lectureRepository.findByIdSection(lecturePutVM.id())).thenReturn(Optional.empty());

        // When: update is called with the LecturePostVM and the lectureId.
        // Then: A NotFoundException is thrown with the error code LECTURE_NOT_FOUND.
        assertThrows(NotFoundException.class, () -> lectureService.update(lecturePutVM, lecturePutVM.id()));
    }
    @Test
    void shouldThrowBadRequestException_whenSectionIdMismatch() {
        // Given: A LecturePostVM with a different sectionId than the existing lecture's sectionId.
        Section section = new Section();
        section.setId(2L); // Different from the provided sectionId in LecturePostVM
        Lecture lecture = new Lecture();
        lecture.setSection(section);
        LecturePostVM lecturePutVM = new LecturePostVM(1L, "Updated Title", 2.0f, "Updated Details", 75, "NewVideoId", 1L);
        when(lectureRepository.findByIdSection(lecturePutVM.id())).thenReturn(Optional.of(lecture));

        // When: update is called with the LecturePostVM.
        // Then: A BadRequestException is thrown with the error code LECTURE_SECTION_NOT_SAME.
        assertThrows(BadRequestException.class, () -> lectureService.update(lecturePutVM, lecturePutVM.id()));
    }

    @Test
    void shouldUpdateLectureSuccessfully_whenValidLecturePutVMProvided() {
        // Given: A valid LecturePostVM with matching sectionId and an existing lecture.
        Section section = new Section();
        section.setId(1L);
        Lecture lecture = new Lecture();
        lecture.setSection(section);
        LecturePostVM lecturePutVM = new LecturePostVM(1L, "Updated Title", 2.0f, "Updated Details", 75, "NewVideoId", 1L);
        when(lectureRepository.findByIdSection(lecturePutVM.id())).thenReturn(Optional.of(lecture));
        when(lectureRepository.saveAndFlush(Mockito.any(Lecture.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When: update is called with the LecturePostVM and the lectureId.
        LectureVm lectureVm = lectureService.update(lecturePutVM, lecturePutVM.id());

        // Then: The lecture is updated with the new details and saved in the repository.
        assertNotNull(lectureVm);
        assertEquals(lecturePutVM.title(), lectureVm.getTitle());
        verify(lectureRepository).saveAndFlush(Mockito.any(Lecture.class));
    }

    @Test
    void shouldDeleteLectureSuccessfully_whenLectureExists() {
        // Given: A lectureId for an existing lecture.
        doNothing().when(lectureRepository).deleteById(anyLong());

        // When: delete is called with the lectureId.
        lectureService.delete(1L);

        // Then: The lecture is deleted from the repository.
        verify(lectureRepository).deleteById(1L);
    }
}
