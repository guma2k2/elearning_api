package com.backend.elearning.domain.auth;

public record AuthenticationResponse (
        String token,
        boolean authenticated
) {
}
