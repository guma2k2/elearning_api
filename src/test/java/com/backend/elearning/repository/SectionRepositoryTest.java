package com.backend.elearning.repository;

import com.backend.elearning.TestConfig;
import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.lecture.LectureRepository;
import com.backend.elearning.domain.quiz.Quiz;
import com.backend.elearning.domain.quiz.QuizRepository;
import com.backend.elearning.domain.section.Section;
import com.backend.elearning.domain.section.SectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({TestConfig.class})
public class SectionRepositoryTest {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private QuizRepository quizRepository;

    @BeforeEach
    void setUp() {
        sectionRepository.deleteAll();
        lectureRepository.deleteAll();
        quizRepository.deleteAll();

        // Setting up test data
        Section section1 = Section.builder()
                .title("Section 1")
                .build();

        Section section2 = Section.builder()
                .title("Section 2")
                .build();

        Lecture lecture1 = Lecture.builder()
                .title("Lecture 1")
                .section(section1)
                .build();

        Lecture lecture2 = Lecture.builder()
                .title("Lecture 2")
                .section(section1)
                .build();

        Quiz quiz1 = Quiz.builder()
                .title("Quiz 1")
                .section(section2)
                .build();

        Quiz quiz2 = Quiz.builder()
                .title("Quiz 2")
                .section(section2)
                .build();

        sectionRepository.saveAll(List.of(section1, section2));
        lectureRepository.saveAll(List.of(lecture1, lecture2));
        quizRepository.saveAll(List.of(quiz1, quiz2));

        // Re-attaching the entities to update their relationships
        section1.getLectures().addAll(List.of(lecture1, lecture2));
        section2.getQuizzes().addAll(List.of(quiz1, quiz2));

        sectionRepository.saveAll(List.of(section1, section2));
    }

    @Test
    @Transactional
    void testFindByIdLectures_WithExistingId() {
        // Given
        Long id = sectionRepository.findAll().get(0).getId();

        // When
        Optional<Section> result = sectionRepository.findByIdLectures(id);

        // Then
        assertTrue(result.isPresent(), "Section should be found with the given ID");
        assertFalse(result.get().getLectures().isEmpty(), "The section should have lectures");
    }

    @Test
    void testFindByIdLectures_WithNonExistingId() {
        // Given
        Long id = 999L;  // Non-existing ID

        // When
        Optional<Section> result = sectionRepository.findByIdLectures(id);

        // Then
        assertFalse(result.isPresent(), "No section should be found with a non-existing ID");
    }

    @Test
    @Transactional
    void testFindByIdQuizzes_WithExistingSection() {
        // Given
        Section section = sectionRepository.findAll().get(1);

        // When
        Optional<Section> result = sectionRepository.findByIdQuizzes(section);

        // Then
        assertTrue(result.isPresent(), "Section should be found with the given section");
        assertFalse(result.get().getQuizzes().isEmpty(), "The section should have quizzes");
    }

    @Test
    void testFindByIdQuizzes_WithNonExistingSection() {
        // Given
        Section section = Section.builder().id(999L).build();  // Non-existing section

        // When
        Optional<Section> result = sectionRepository.findByIdQuizzes(section);

        // Then
        assertFalse(result.isPresent(), "No section should be found with a non-existing section");
    }
}
