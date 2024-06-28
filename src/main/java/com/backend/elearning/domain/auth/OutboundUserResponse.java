package com.backend.elearning.domain.auth;

public record OutboundUserResponse(
        String email,
        String givenName,
        String familyName
) {
}
