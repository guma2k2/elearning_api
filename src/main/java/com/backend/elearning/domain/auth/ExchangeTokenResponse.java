package com.backend.elearning.domain.auth;

public record ExchangeTokenResponse(
        String accessToken,
        Long expiresIn,
        String refreshToken,
        String scope,
        String tokenType
) {
}
