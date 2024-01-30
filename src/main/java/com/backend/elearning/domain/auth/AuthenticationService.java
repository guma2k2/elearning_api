package com.backend.elearning.domain.auth;


import com.backend.elearning.domain.media.MediaService;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserRepository;
import com.backend.elearning.domain.user.UserVm;
import com.backend.elearning.security.AuthUserDetails;
import com.backend.elearning.security.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JWTUtil jwtUtil;

    private final MediaService mediaService;

    public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository, JWTUtil jwtUtil, MediaService mediaService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.mediaService = mediaService;
    }

    public AuthenticationVm login(AuthenticationPostVm request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        AuthUserDetails principal = (AuthUserDetails) authentication.getPrincipal();
        User user = userRepository.findById(principal.getId()).orElseThrow();
        String urlPhoto = mediaService.getUrlById(user.getPhotoId());
        UserVm userVm = UserVm.fromModel(user, urlPhoto);
        String token = jwtUtil.issueToken(request.username(), principal.getRole());
        return new AuthenticationVm(token, userVm);
    }
}
