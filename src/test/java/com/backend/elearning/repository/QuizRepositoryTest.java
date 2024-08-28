package com.backend.elearning.repository;

import com.backend.elearning.TestConfig;
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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TestConfig.class})
public class QuizRepositoryTest {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @BeforeEach
    void setUp() {
        // Clearing the repository before each test
        quizRepository.deleteAll();
        sectionRepository.deleteAll();

        // Setting up test data
        Section section1 = Section.builder()
                .title("Section 1")
                .build();

        Section section2 = Section.builder()
                .title("Section 2")
                .build();

        sectionRepository.saveAll(List.of(section1, section2));

        Quiz quiz1 = Quiz.builder()
                .title("Quiz 1")
                .section(section1)
                .build();

        Quiz quiz2 = Quiz.builder()
                .title("Quiz 2")
                .section(section1)
                .build();

        Quiz quiz3 = Quiz.builder()
                .title("Quiz 3")
                .section(section2)
                .build();

        quizRepository.saveAll(List.of(quiz1, quiz2, quiz3));
    }

    @Test
    @Transactional
    void testFindByIdReturnSection_WithExistingId() {
        // Given
        Quiz quiz = quizRepository.findAll().get(0);
        Long id = quiz.getId();

        // When
        Optional<Quiz> result = quizRepository.findByIdReturnSection(id);

        // Then
        assertTrue(result.isPresent(), "Quiz should be found with the given ID");
        assertEquals(quiz.getTitle(), result.get().getTitle(), "The quiz title should match");
        assertNotNull(result.get().getSection(), "The quiz should have a section");
    }

    @Test
    void testFindByIdReturnSection_WithNonExistingId() {
        // Given
        Long id = 999L;  // Non-existing ID

        // When
        Optional<Quiz> result = quizRepository.findByIdReturnSection(id);

        // Then
        assertFalse(result.isPresent(), "No quiz should be found with a non-existing ID");
    }

    @Test
    void testFindBySectionId_WithExistingSection() {
        // Given
        Section section = sectionRepository.findAll().get(0);
        Long sectionId = section.getId();

        // When
        List<Quiz> result = quizRepository.findBySectionId(sectionId);

        // Then
        assertFalse(result.isEmpty(), "Quizzes should be found for the given section ID");
        assertTrue(result.stream().allMatch(q -> q.getSection().getId().equals(sectionId)),
                "All quizzes should belong to the given section");
    }

    @Test
    void testFindBySectionId_WithNonExistingSection() {
        // Given
        Long sectionId = 999L;  // Non-existing section ID

        // When
        List<Quiz> result = quizRepository.findBySectionId(sectionId);

        // Then
        assertTrue(result.isEmpty(), "No quizzes should be found for a non-existing section ID");
    }
}
