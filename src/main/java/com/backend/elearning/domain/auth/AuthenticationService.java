package com.backend.elearning.domain.auth;


import com.backend.elearning.domain.mail.MailService;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.domain.user.ERole;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserRepository;
import com.backend.elearning.domain.user.UserVm;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.security.JWTUtil;
import com.backend.elearning.utils.Constants;
import com.backend.elearning.utils.RandomString;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class AuthenticationService {


    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    private static final  String EXCHANGE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String EXCHANGE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    @Value("${outbound.identity.client-id}")
    protected String CLIENT_ID;

    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;

    @Value("${outbound.identity.redirect-uri}")
    protected String REDIRECT_URI;

    @Value("${ui.forgot-password.url}")
    protected String FORGOTPASSWORD_LINK;

    private static final String GRANT_TYPE = "authorization_code";

    public AuthenticationService(UserRepository userRepository, JWTUtil jwtUtil, RestTemplate restTemplate, StudentRepository studentRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;
        this.studentRepository = studentRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    public AuthenticationVm login(AuthenticationPostVm request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        Optional<User> user = userRepository.findByEmail(request.email());
        Optional<Student> student = studentRepository.findByEmail(request.email());
        if (!user.isPresent() && !student.isPresent()) {
            throw new BadRequestException("Email or password is not corrected");
        }
        if (user.isPresent()) {
            String token = jwtUtil.issueToken(request.email(), user.get().getRole().name());
            UserVm userVm = UserVm.fromModel(user.get());
            return new AuthenticationVm(token, userVm);
        }
        String token = jwtUtil.issueToken(request.email(), ERole.ROLE_STUDENT.name());
        UserVm userVm = UserVm.fromModelStudent(student.get());
        return new AuthenticationVm(token, userVm);
    }

    public AuthenticationVm outboundAuthenticate(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("client_id", CLIENT_ID);
        map.add("client_secret", CLIENT_SECRET);
        map.add("redirect_uri", REDIRECT_URI);
        map.add("grant_type", GRANT_TYPE);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ExchangeTokenResponse response = restTemplate.exchange(EXCHANGE_TOKEN_URL, HttpMethod.POST, entity, ExchangeTokenResponse.class).getBody();

        if (response != null) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(EXCHANGE_USER_INFO_URL)
                    .queryParam("alt", "json")
                    .queryParam("access_token", response.accessToken());

            // Perform the GET request directly without HttpEntity
            OutboundUserResponse userInfo = restTemplate.getForObject(builder.toUriString(), OutboundUserResponse.class);

            if (userInfo != null) {
                Student student = studentRepository.findByEmail(userInfo.email()).orElseGet(
                        () -> studentRepository.saveAndFlush(Student.builder()
                                        .active(true)
                                        .firstName(userInfo.familyName())
                                        .lastName(userInfo.givenName())
                                        .email(userInfo.email())
                                        .photo(userInfo.picture())
                                .build()));
                String token = jwtUtil.issueToken(student.getEmail(), ERole.ROLE_STUDENT.name());
                UserVm userVm = UserVm.fromModelStudent(student);
                AuthenticationVm authenticationVm = new AuthenticationVm(token, userVm);
                return authenticationVm ;
            }
        }

        return null;
    }

    public AuthenticationVm register(RegistrationPostVm request) {
        Long checkExisted = studentRepository.countByExistedEmail(request.email(), null);
        if (checkExisted > 0) {
            throw new DuplicateException(Constants.ERROR_CODE.USER_EMAIL_DUPLICATED, request.email());
        }
        Student student = Student.builder()
                .active(true)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .photo("")
                .active(false)
                .build();
        student.setVerificationCode(generateVerificationCode());
        student.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        studentRepository.saveAndFlush(student);
        sendVerificationEmail(student);
        String token = jwtUtil.issueToken(student.getEmail(), ERole.ROLE_STUDENT.name());
        UserVm userVm = UserVm.fromModelStudent(student);
        AuthenticationVm authenticationVm = new AuthenticationVm(token, userVm);
        return authenticationVm ;
    }

    private void sendVerificationEmail(Student student) {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + student.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            mailService.sendEmail(student.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }


    public void verify(VerifyStudentVM request) {
        Optional<Student> studentOptional = studentRepository.findByEmail(request.email());
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            if (student.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Verification code has expired");
            }
            if (student.getVerificationCode().equals(student.getVerificationCode())) {
                student.setActive(true);
                student.setVerificationCode(null);
                student.setVerificationCodeExpiresAt(null);
                studentRepository.save(student);
            } else {
                throw new BadRequestException("Invalid verification code");
            }
        } else {
            throw new NotFoundException("Student not found");
        }
    }

    public void forgotPassword(String email) {
        Optional<Student> studentOptional = studentRepository.findByEmail(email);
        if (!studentOptional.isPresent()) {
            throw new NotFoundException(Constants.ERROR_CODE.STUDENT_NOT_FOUND, email);
        }
        String toAddress = email;
        String url = FORGOTPASSWORD_LINK + "?email=" + email;
        String subject = "Confirm and change password" ;
        String content = "<p>Hi,</p>" +
                "<p> You requested to change your password successful </p>" +
                "Please click the link below to change your password:" +
                "<p> <a href =\"" + url + "\">Change password</a> </p>" +
                "<br>" +
                "<p>Delete this email if you want, after you change your password successful</p>";
        try {
            mailService.sendEmail(toAddress, content, subject);
        } catch (MessagingException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public void updatePassword(AuthenticationPostVm request) {
        try {
            Student student = studentRepository
                    .findByEmail(request.email())
                    .orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.USER_NOT_FOUND, request.email()));
            student.setPassword(passwordEncoder.encode(request.password()));
            student.setUpdatedAt(LocalDateTime.now());
            studentRepository.save(student);
        } catch (Exception e) {
            // Log the exception to understand the issue
            log.error("Error updating password", e);
            throw e; // Rethrow the exception if needed
        }
    }

    public AuthenticationVm outboundAuthenticateForMobile(OutboundUserRequest userInfo) {
        Student student = studentRepository.findByEmail(userInfo.email()).orElseGet(
                () -> studentRepository.saveAndFlush(Student.builder()
                        .active(true)
                        .firstName(userInfo.familyName())
                        .lastName(userInfo.givenName())
                        .email(userInfo.email())
                        .photo(userInfo.picture())
                        .build()));
        String token = jwtUtil.issueToken(student.getEmail(), ERole.ROLE_STUDENT.name());
        UserVm userVm = UserVm.fromModelStudent(student);
        AuthenticationVm authenticationVm = new AuthenticationVm(token, userVm);
        return authenticationVm ;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

}
