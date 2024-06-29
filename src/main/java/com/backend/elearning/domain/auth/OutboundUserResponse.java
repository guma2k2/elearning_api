package com.backend.elearning.domain.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OutboundUserResponse(
        String email,
        String givenName,
        String familyName,
        String picture
) {
}
