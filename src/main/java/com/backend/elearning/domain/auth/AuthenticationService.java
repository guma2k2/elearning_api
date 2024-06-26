package com.backend.elearning.domain.auth;


import com.backend.elearning.domain.media.MediaService;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.domain.user.ERole;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserRepository;
import com.backend.elearning.domain.user.UserVm;
import com.backend.elearning.security.AuthUserDetails;
import com.backend.elearning.security.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JWTUtil jwtUtil;

    private final StudentRepository studentRepository;

    public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository, JWTUtil jwtUtil, StudentRepository studentRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
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
}
