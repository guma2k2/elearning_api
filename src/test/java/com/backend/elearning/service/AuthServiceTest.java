package com.backend.elearning.service;

import com.backend.elearning.domain.auth.AuthenticationPostVm;
import com.backend.elearning.domain.auth.AuthenticationService;
import com.backend.elearning.domain.auth.AuthenticationVm;
import com.backend.elearning.domain.category.CategoryServiceImpl;
import com.backend.elearning.domain.mail.MailService;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.domain.user.EGender;
import com.backend.elearning.domain.user.ERole;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserRepository;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.security.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailService mailService;

    private AuthenticationService authenticationService;

    @BeforeEach
    void beforeEach() {
        authenticationService = new AuthenticationService(userRepository, jwtUtil, restTemplate, studentRepository,
                 authenticationManager, passwordEncoder, mailService);
    }

    @Test
    void shouldAuthenticateAndReturnUserAuthenticationVm() {
        // Given
        String email = "user@example.com";
        String password = "password";
        ERole role = ERole.ROLE_STUDENT;

        AuthenticationPostVm request = new AuthenticationPostVm(email, password);
        User user = User.builder().email(email).role(role).gender(EGender.FEMALE).dateOfBirth(LocalDate.of(2002, 12,12)).build();

        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(studentRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(jwtUtil.issueToken(email, role.name())).thenReturn("mocked-token");

        // When
        AuthenticationVm result = authenticationService.login(request);

        // Then
        assertNotNull(result);
        assertEquals("mocked-token", result.token());
        assertEquals(email, result.user().email());
        assertEquals(role.name(), result.user().role());
    }

    @Test
    void shouldAuthenticateAndReturnStudentAuthenticationVm() {
        // Given
        String email = "student@example.com";
        String password = "password";

        AuthenticationPostVm request = new AuthenticationPostVm(email, password);
        Student student = Student.builder().email(email).gender(EGender.FEMALE).dateOfBirth(LocalDate.of(2002, 12,12)).build();

        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(student));
        when(jwtUtil.issueToken(email, ERole.ROLE_STUDENT.name())).thenReturn("mocked-token");

        // When
        AuthenticationVm result = authenticationService.login(request);

        // Then
        assertNotNull(result);
        assertEquals("mocked-token", result.token());
        assertEquals(email, result.user().email());
        assertEquals(ERole.ROLE_STUDENT.name(), result.user().role());
    }


    @Test
    void login_shouldReturnAuthenticationVmForUser_whenValidUserCredentialsProvided() {
        // Given
        String email = "user@example.com";
        String password = "password";
        ERole role = ERole.ROLE_STUDENT;

        AuthenticationPostVm request = new AuthenticationPostVm(email, password);
        User user = User.builder().email(email).role(role).gender(EGender.FEMALE).dateOfBirth(LocalDate.of(2002, 12,12)).build();

        // When the authentication process is called, it does not throw any exceptions
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        // When the user repository is queried, it returns a valid user
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When the student repository is queried, it returns an empty result
        when(studentRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When the JWT utility is called to issue a token, it returns a mocked token
        when(jwtUtil.issueToken(email, role.name())).thenReturn("mocked-token");

        // When
        AuthenticationVm result = authenticationService.login(request);

        // Then
        assertNotNull(result);
        assertEquals("mocked-token", result.token());
        assertEquals(email, result.user().email());
        assertEquals(role.name(), result.user().role());
    }

    @Test
    void login_shouldReturnAuthenticationVmForStudent_whenValidStudentCredentialsProvided() {
        // Given
        String email = "student@example.com";
        String password = "password";

        AuthenticationPostVm request = new AuthenticationPostVm(email, password);
        Student student = Student.builder().email(email).gender(EGender.FEMALE).dateOfBirth(LocalDate.of(2002, 12,12)).build();

        // When the authentication process is called, it does not throw any exceptions
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        // When the user repository is queried, it returns an empty result
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When the student repository is queried, it returns a valid student
        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(student));

        // When the JWT utility is called to issue a token, it returns a mocked token
        when(jwtUtil.issueToken(email, ERole.ROLE_STUDENT.name())).thenReturn("mocked-token");

        // When
        AuthenticationVm result = authenticationService.login(request);

        // Then
        assertNotNull(result);
        assertEquals("mocked-token", result.token());
        assertEquals(email, result.user().email());
        assertEquals(ERole.ROLE_STUDENT.name(), result.user().role());
    }

}
