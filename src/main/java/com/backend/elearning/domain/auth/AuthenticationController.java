package com.backend.elearning.domain.auth;


import com.backend.elearning.domain.student.StudentVm;
import com.backend.elearning.domain.user.UserVm;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/v1/auth")
@RestController
@Validated
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/forgotpassword")
    @Operation(method = "POST", summary = "Recover password", description ="Send a request via this API to send an email to recover password" )
    public ResponseEntity<Void> forgotPassword (
            @Param("email") String email
    ) {
        authenticationService.forgotPassword(email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    @Operation(method = "PUT", summary = "Update password", description ="Send a request via this API to update password" )
    public ResponseEntity<Void> forgotPassword (
            @Valid @RequestBody AuthenticationPostVm request
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

    @PostMapping("/outbound/authentication/mobile")
    ResponseEntity<AuthenticationVm> outboundAuthenticateForMobile(
            @RequestBody OutboundUserRequest request
    ){
        AuthenticationVm result = authenticationService.outboundAuthenticateForMobile(request);
        return ResponseEntity.ok().body(result);
    }



    @PostMapping("/login")
    @Operation(method = "POST", summary = "Send request to sign in system", description ="Send a request via this API to login" )
    public ResponseEntity<AuthenticationVm> login (
            @RequestBody AuthenticationPostVm request
    ) {
        AuthenticationVm response = authenticationService.login(request);
        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/register")
    @Operation(method = "POST", summary = "Send request to create new user", description ="Send a request via this API to create new user" )
    public ResponseEntity<AuthenticationVm> register (
            @RequestBody RegistrationPostVm request
    ) {
        AuthenticationVm response = authenticationService.register(request);
        return ResponseEntity.ok()
                .body(response);
    }



    @PostMapping("/verify")
    public ResponseEntity<String> verify (
            @RequestBody VerifyStudentVM request
    ) {
        String response = authenticationService.verify(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/resend")
    @Operation(method = "POST", summary = "Resend verificationCode to recover password user", description ="Send a request via this API to resend token" )
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        authenticationService.resendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent");
    }

}
