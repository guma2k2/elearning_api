package com.backend.elearning.domain.auth;

public record OutboundUserRequest(String email, String givenName, String familyName, String picture) {
}
