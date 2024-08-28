package com.backend.elearning.repository;

import com.backend.elearning.TestConfig;
import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.lecture.LectureRepository;
import com.backend.elearning.domain.section.Section;
import com.backend.elearning.domain.section.SectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TestConfig.class})
public class LectureRepositoryTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @BeforeEach
    void setUp() {
        // Clearing the repository before each test
        lectureRepository.deleteAll();
        sectionRepository.deleteAll();

        // Setting up test data
        Section section1 = Section.builder()
                .title("Section 1")
                .build();

        Section section2 = Section.builder()
                .title("Section 2")
                .build();

        sectionRepository.saveAll(List.of(section1, section2));

        Lecture lecture1 = Lecture.builder()
                .title("Lecture 1")
                .section(section1)
                .build();

        Lecture lecture2 = Lecture.builder()
                .title("Lecture 2")
                .section(section1)
                .build();

        Lecture lecture3 = Lecture.builder()
                .title("Lecture 3")
                .section(section2)
                .build();

        lectureRepository.saveAll(List.of(lecture1, lecture2, lecture3));
    }

    @Test
    void testFindByIdSection_WithExistingId() {
        // Given
        Lecture lecture = lectureRepository.findAll().get(0);
        Long id = lecture.getId();

        // When
        Optional<Lecture> result = lectureRepository.findByIdSection(id);

        // Then
        assertTrue(result.isPresent(), "Lecture should be found with the given ID");
        assertEquals(lecture.getTitle(), result.get().getTitle(), "The lecture title should match");
        assertNotNull(result.get().getSection(), "The lecture should have a section");
    }

    @Test
    void testFindByIdSection_WithNonExistingId() {
        // Given
        Long id = 999L;  // Non-existing ID

        // When
        Optional<Lecture> result = lectureRepository.findByIdSection(id);

        // Then
        assertFalse(result.isPresent(), "No lecture should be found with a non-existing ID");
    }

    @Test
    void testFindBySectionId_WithExistingSection() {
        // Given
        Section section = sectionRepository.findAll().get(0);
        Long sectionId = section.getId();

        // When
        List<Lecture> result = lectureRepository.findBySectionId(sectionId);

        // Then
        assertFalse(result.isEmpty(), "Lectures should be found for the given section ID");
        assertTrue(result.stream().allMatch(l -> l.getSection().getId().equals(sectionId)),
                "All lectures should belong to the given section");
    }

    @Test
    void testFindBySectionId_WithNonExistingSection() {
        // Given
        Long sectionId = 999L;  // Non-existing section ID

        // When
        List<Lecture> result = lectureRepository.findBySectionId(sectionId);

        // Then
        assertTrue(result.isEmpty(), "No lectures should be found for a non-existing section ID");
    }
}
