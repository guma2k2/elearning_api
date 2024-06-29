package com.backend.elearning.domain.auth;

public record RegistrationPostVm(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
