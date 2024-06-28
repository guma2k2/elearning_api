package com.backend.elearning.domain.auth;


import com.backend.elearning.domain.student.StudentVm;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/v1/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/outbound/authentication")
    ResponseEntity<AuthenticationVm<StudentVm>> outboundAuthenticate(
            @RequestParam("code") String code
    ){
        AuthenticationVm<StudentVm> result = authenticationService.outboundAuthenticate(code);
        return ResponseEntity.ok().body(result);
    }

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationVm> login (
            @RequestBody AuthenticationPostVm request
    ) {
        AuthenticationVm response = authenticationService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.token())
                .body(response);
    }

}
