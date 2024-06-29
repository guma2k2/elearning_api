package com.backend.elearning.domain.auth;


import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.domain.student.StudentVm;
import com.backend.elearning.domain.user.ERole;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserRepository;
import com.backend.elearning.domain.user.UserVm;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.security.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
@Slf4j
public class AuthenticationService {


    private final UserRepository userRepository;


    private final StudentRepository studentRepository;
    private final JWTUtil jwtUtil;

    private final RestTemplate restTemplate;



    private final String EXCHANGE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String EXCHANGE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    @Value("${outbound.identity.client-id}")
    protected String CLIENT_ID;

    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;

    @Value("${outbound.identity.redirect-uri}")
    protected String REDIRECT_URI;

    private final String GRANT_TYPE = "authorization_code";

    public AuthenticationService(UserRepository userRepository, JWTUtil jwtUtil, RestTemplate restTemplate, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;
        this.studentRepository = studentRepository;
    }

    public AuthenticationVm login(AuthenticationPostVm request) {
        Optional<User> user = userRepository.findByEmail(request.username());
        if (user.isPresent()) {
            String token = jwtUtil.issueToken(request.username(), user.get().getRole().name());
            UserVm userVm = UserVm.fromModel(user.get(), "");
            return new AuthenticationVm(token, userVm);
        }
        Optional<Student> student = studentRepository.findByEmail(request.username());
        String token = jwtUtil.issueToken(request.username(), ERole.ROLE_STUDENT.name());
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
            log.info(response.toString());
            log.info(response.toString());
        }


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
        Student student = studentRepository.findByEmail(request.email()).orElseGet(
                () -> studentRepository.saveAndFlush(Student.builder()
                        .active(true)
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .email(request.email())
                        .photo("")
                        .build()));
        String token = jwtUtil.issueToken(student.getEmail(), ERole.ROLE_STUDENT.name());
        UserVm userVm = UserVm.fromModelStudent(student);
        AuthenticationVm authenticationVm = new AuthenticationVm(token, userVm);
        return authenticationVm ;
    }
}
