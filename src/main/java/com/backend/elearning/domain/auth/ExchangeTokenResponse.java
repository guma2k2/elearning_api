package com.backend.elearning.domain.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ExchangeTokenResponse(
        String accessToken,
        Long expiresIn,
        String refreshToken,
        String scope,
        String tokenType
) {
}
