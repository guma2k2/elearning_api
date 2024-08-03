package com.backend.elearning.domain.auth;


import com.backend.elearning.domain.student.StudentVm;
import com.backend.elearning.domain.user.UserVm;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/v1/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity<Void> forgotPassword (
            @Param("email") String email
    ) {
        authenticationService.forgotPassword(email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> forgotPassword (
            @RequestBody AuthenticationPostVm request
    ) {
        authenticationService.updatePassword(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/outbound/authentication")
    ResponseEntity<AuthenticationVm> outboundAuthenticate(
            @RequestParam("code") String code
    ){
        AuthenticationVm result = authenticationService.outboundAuthenticate(code);
        return ResponseEntity.ok().body(result);
    }



    @PostMapping("/login")
    public ResponseEntity<AuthenticationVm> login (
            @RequestBody AuthenticationPostVm request
    ) {
        AuthenticationVm response = authenticationService.login(request);
        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationVm> register (
            @RequestBody RegistrationPostVm request
    ) {
        AuthenticationVm response = authenticationService.register(request);
        return ResponseEntity.ok()
                .body(response);
    }

}
