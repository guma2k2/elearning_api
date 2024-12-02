package com.backend.elearning.service;

import com.backend.elearning.domain.student.*;
import com.backend.elearning.domain.user.UserServiceImpl;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private StudentService studentService;
    @BeforeEach
    void beforeEach() {
        studentService = new StudentServiceImpl(studentRepository, passwordEncoder);
    }
    @Test
    void shouldThrowDuplicateException_whenUpdatingStudentWithDuplicateEmail() {
        StudentPutVM studentPutVM = new StudentPutVM(1L, "duplicate@example.com", "John", "Doe", "password", "Male", "photo.png", 1, 1, 1990);
        when(studentRepository.countByExistedEmail(studentPutVM.email(), studentPutVM.id())).thenReturn(1L);

        assertThrows(DuplicateException.class, () -> studentService.updateProfileStudent(studentPutVM));
    }

    @Test
    void shouldThrowNotFoundException_whenUpdatingNonExistentStudent() {
        StudentPutVM studentPutVM = new StudentPutVM(999L, "newemail@example.com", "John", "Doe", "password", "Male", "photo.png", 1, 1, 1990);
        when(studentRepository.countByExistedEmail(studentPutVM.email(), studentPutVM.id())).thenReturn(0L);
        when(studentRepository.findById(studentPutVM.id())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> studentService.updateProfileStudent(studentPutVM));
    }
    @Test
    void shouldUpdateStudentDetailsSuccessfully_whenValidStudentPutVMProvided() {
        StudentPutVM studentPutVM = new StudentPutVM(1L, "newemail@example.com", "John", "Doe", "password", "MALE", "photo.png", 1, 1, 1990);
        Student student = new Student();
        when(studentRepository.countByExistedEmail(studentPutVM.email(), studentPutVM.id())).thenReturn(0L);
        when(studentRepository.findById(studentPutVM.id())).thenReturn(Optional.of(student));
        when(passwordEncoder.encode(studentPutVM.password())).thenReturn("encodedPassword");

        studentService.updateProfileStudent(studentPutVM);

        assertEquals(studentPutVM.email(), student.getEmail());
        assertEquals(studentPutVM.firstName(), student.getFirstName());
        assertEquals(studentPutVM.lastName(), student.getLastName());
        assertEquals("photo.png", student.getPhoto());
        assertEquals("encodedPassword", student.getPassword());
        verify(studentRepository).saveAndFlush(student);
    }

    @Test
    void shouldNotUpdatePhoto_whenPhotoIsEmptyOrNull() {
        StudentPutVM studentPutVM = new StudentPutVM(1L, "newemail@example.com", "John", "Doe", "password", "MALE", "", 1, 1, 1990);
        Student student = new Student();
        student.setPhoto("existing_photo.png");
        when(studentRepository.countByExistedEmail(studentPutVM.email(), studentPutVM.id())).thenReturn(0L);
        when(studentRepository.findById(studentPutVM.id())).thenReturn(Optional.of(student));

        studentService.updateProfileStudent(studentPutVM);

        assertEquals("existing_photo.png", student.getPhoto());
        verify(studentRepository).saveAndFlush(student);
    }

    @Test
    void shouldNotUpdatePassword_whenPasswordIsEmptyOrNull() {
        StudentPutVM studentPutVM = new StudentPutVM(1L, "newemail@example.com", "John", "Doe", "", "MALE", "photo.png", 1, 1, 1990);
        Student student = new Student();
        student.setPassword("existing_password");
        when(studentRepository.countByExistedEmail(studentPutVM.email(), studentPutVM.id())).thenReturn(0L);
        when(studentRepository.findById(studentPutVM.id())).thenReturn(Optional.of(student));

        studentService.updateProfileStudent(studentPutVM);

        assertEquals("existing_password", student.getPassword());
        verify(studentRepository).saveAndFlush(student);
    }
}
