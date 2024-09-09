package com.backend.elearning.repository;

import com.backend.elearning.TestConfig;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import({TestConfig.class})
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        // Clear the repository before each test
        studentRepository.deleteAll();

        // Setting up test data
        Student student1 = Student.builder()
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("1234567")
                .build();

        Student student2 = Student.builder()
                .email("jane.smith@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("1234567")

                .build();

        Student student3 = Student.builder()
                .id(3L)
                .email("john.smith@example.com")
                .firstName("John")
                .lastName("Smith")
                .password("1234567")
                .build();

        // Save entities
        studentRepository.saveAllAndFlush(List.of(student1, student2, student3));
    }

    @Test
    void testFindAllWithKeyword_WithExistingKeyword() {
        // Given
        String keyword = "john";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Student> result = studentRepository.findAllWithKeyword(pageable, keyword);

        // Then
        assertEquals(2, result.getTotalElements(), "There should be 2 students matching the keyword 'john'");
    }

    @Test
    void testFindAllWithKeyword_WithNoMatchingKeyword() {
        // Given
        String keyword = "nonexistent";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Student> result = studentRepository.findAllWithKeyword(pageable, keyword);

        // Then
        assertEquals(0, result.getTotalElements(), "There should be no students matching the keyword 'nonexistent'");
    }

    @Test
    void testCountByExistedEmail_WithExistingEmailAndNoId() {
        // Given
        String email = "john.doe@example.com";
        Long id = null;  // Checking for existence without ID constraint

        // When
        Long count = studentRepository.countByExistedEmail(email, id);

        // Then
        assertEquals(1, count, "There should be 1 student with the email 'john.doe@example.com'");
    }



    @Test
    void testCountByExistedEmail_WithNonExistingEmail() {
        // Given
        String email = "nonexistent@example.com";
        Long id = null;  // Checking for non-existing email

        // When
        Long count = studentRepository.countByExistedEmail(email, id);

        // Then
        assertEquals(0, count, "There should be no students with the email 'nonexistent@example.com'");
    }
}
