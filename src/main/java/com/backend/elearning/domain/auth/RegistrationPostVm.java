package com.backend.elearning.domain.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RegistrationPostVm(
        @NotBlank(message = "first name must not be blank")
        String firstName,
        @NotBlank(message = "last name must not be blank")
        String lastName,
        @Email(message = "email invalid format")
        String email,

        @Min(message = "password need at least 6 character", value = 6)
        String password
) {
}
