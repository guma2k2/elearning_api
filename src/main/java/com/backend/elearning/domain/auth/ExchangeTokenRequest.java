package com.backend.elearning.domain.auth;

public record ExchangeTokenRequest(
        String code,
        String clientId,
        String clientSecret,
        String redirectUri,
        String grantType
) {
}
