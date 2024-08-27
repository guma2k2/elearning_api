package com.backend.elearning.service;

import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.learning.learningCourse.LearningCourseRepository;
import com.backend.elearning.domain.review.ReviewService;
import com.backend.elearning.domain.topic.TopicServiceImpl;
import com.backend.elearning.domain.user.*;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import com.backend.elearning.utils.MessageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ReviewService reviewService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private LearningCourseRepository learningCourseRepository;

    private UserService userService;

    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(userRepository, reviewService, passwordEncoder, learningCourseRepository);
    }

    @Test
    void createUser_shouldReturnUserVm_whenUserIsCreatedSuccessfully() {
        // given
        UserPostVm userPostVm = new UserPostVm(
                "test@example.com",
                "John",
                "Doe",
                "password123",
                EGender.MALE.name(),
                true ,
                "ADMIN",
                15,
                5,
                1990,
                "ROLE_ADMIN"
        );

        User newUser = User.builder()
                .email(userPostVm.email())
                .firstName(userPostVm.firstName())
                .lastName(userPostVm.lastName())
                .active(userPostVm.active())
                .password("encodedPassword")  // Assuming the password is encoded
                .photo(userPostVm.photo())
                .role(ERole.valueOf(userPostVm.role()))
                .dateOfBirth(LocalDate.of(userPostVm.year(), userPostVm.month(), userPostVm.day()))
                .gender(EGender.valueOf(userPostVm.gender()))
                .build();
        User savedUser = User.builder()
                .id(1L)  // Assuming the user is saved with an ID of 1
                .email(newUser.getEmail())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .active(newUser.isActive())
                .dateOfBirth(newUser.getDateOfBirth())
                .password(newUser.getPassword())
                .photo(newUser.getPhoto())
                .role(newUser.getRole())
                .gender(newUser.getGender())
                .build();

        when(userRepository.countByExistedEmail(userPostVm.email(), null)).thenReturn(0L);
        when(passwordEncoder.encode(userPostVm.password())).thenReturn("encodedPassword");
        when(userRepository.saveAndFlush(Mockito.any(User.class))).thenReturn(savedUser);

        // when
        UserVm result = userService.create(userPostVm);

        // then
        assertEquals(savedUser.getId(), result.id());
        assertEquals(savedUser.getEmail(), result.email());
        assertEquals(savedUser.getFirstName(), result.firstName());
        assertEquals(savedUser.getLastName(), result.lastName());
        assertEquals(savedUser.isActive(), result.active());
        verify(userRepository, times(1)).saveAndFlush(any(User.class));
    }

    @Test
    void createUser_shouldThrowDuplicateException_whenEmailAlreadyExists() {
        // given
        UserPostVm userPostVm = new UserPostVm(
                "test@example.com",
                "John",
                "Doe",
                "password123",
                EGender.MALE.name(),
                true ,
                "ADMIN",
                15,
                5,
                1990,
                "ROLE_ADMIN"
        );

        when(userRepository.countByExistedEmail(userPostVm.email(), null)).thenReturn(1L);

        // when & then
        DuplicateException exception = assertThrows(
                DuplicateException.class,
                () -> userService.create(userPostVm)
        );

        String exptectMessage = MessageUtil.getMessage(Constants.ERROR_CODE.USER_EMAIL_DUPLICATED, userPostVm.email());
        assertEquals(exptectMessage, exception.getMessage());
        verify(userRepository, never()).saveAndFlush(any(User.class));
    }

    @Test
    void shouldThrowDuplicateException_whenEmailAlreadyExists() {
        UserPutVm userPutVm = new UserPutVm(1L, "duplicate@example.com", "John", "Doe", null, "Male", true, null, 1, 1, 1990, "USER");
        Long userId = 1L;
        when(userRepository.countByExistedEmail(userPutVm.email(), userId)).thenReturn(1L);

        assertThrows(DuplicateException.class, () -> userService.update(userPutVm, userId));
    }

    @Test
    void shouldThrowNotFoundException_whenUserIdNotFound() {
        UserPutVm userPutVm = new UserPutVm(1L, "newemail@example.com", "John", "Doe", null, "Male", true, null, 1, 1, 1990, "USER");
        Long userId = 999L;
        when(userRepository.countByExistedEmail(userPutVm.email(), userId)).thenReturn(0L);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(userPutVm, userId));
    }

    @Test
    void shouldUpdateUserDetails_whenValidUserPutVmAndUserIdProvided() {
        UserPutVm userPutVm = new UserPutVm(1L, "newemail@example.com", "John", "Doe", null, EGender.FEMALE.name(), true, null, 1, 1, 1990, "ROLE_ADMIN");
        Long userId = 1L;
        User user = new User();
        when(userRepository.countByExistedEmail(userPutVm.email(), userId)).thenReturn(0L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.update(userPutVm, userId);

        assertEquals(userPutVm.email(), user.getEmail());
        assertEquals(userPutVm.firstName(), user.getFirstName());
        assertEquals(userPutVm.lastName(), user.getLastName());
        assertEquals(userPutVm.active(), user.isActive());
        assertEquals(ERole.valueOf(userPutVm.role()), user.getRole());
        assertEquals(LocalDate.of(userPutVm.year(), userPutVm.month(), userPutVm.day()), user.getDateOfBirth());
        assertNotNull(user.getUpdatedAt());
        verify(userRepository).save(user);
    }

    @Test
    void shouldUpdatePassword_whenPasswordProvided() {
        UserPutVm userPutVm = new UserPutVm(1L, "newemail@example.com", "John", "Doe", "newpassword", EGender.MALE.name(), true, null, 1, 1, 1990, "ROLE_ADMIN");
        Long userId = 1L;
        User user = new User();
        when(userRepository.countByExistedEmail(userPutVm.email(), userId)).thenReturn(0L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(userPutVm.password())).thenReturn("encodedPassword");

        userService.update(userPutVm, userId);

        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void shouldUpdatePhoto_whenPhotoProvided() {
        UserPutVm userPutVm = new UserPutVm(1L, "newemail@example.com", "John", "Doe", null, EGender.MALE.name(), true, "newphoto.png", 1, 1, 1990, "ROLE_ADMIN");
        Long userId = 1L;
        User user = new User();
        when(userRepository.countByExistedEmail(userPutVm.email(), userId)).thenReturn(0L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.update(userPutVm, userId);

        assertEquals("newphoto.png", user.getPhoto());
        verify(userRepository).save(user);
    }
    @Test
    void shouldThrowNotFoundException_whenDeletingNonExistentUser() {
        Long userId = 999L;
        when(userRepository.findByIdCustom(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.delete(userId));
    }

    @Test
    void shouldThrowBadRequestException_whenDeletingUserWithCourses() {
        Long userId = 1L;
        User user = new User();
        user.setCourses(List.of(new Course())); // Simulating a user with a course
        when(userRepository.findByIdCustom(userId)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.delete(userId));
    }

    @Test
    void shouldDeleteUserSuccessfully_whenUserHasNoCourses() {
        Long userId = 1L;
        User user = new User();
        user.setCourses(Collections.emptyList()); // Simulating a user with no courses
        when(userRepository.findByIdCustom(userId)).thenReturn(Optional.of(user));

        userService.delete(userId);

        verify(userRepository).delete(user);
    }


}
