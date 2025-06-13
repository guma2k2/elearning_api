package com.backend.elearning.domain.user;

import com.backend.elearning.utils.EnumPattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserPostVm(
        @Email(message = "email invalid format")
        String email,
        @NotBlank(message = "first name must not be blank")
        String firstName,
        @NotBlank(message = "last name must not be blank")
        String lastName,
        @NotNull(message = "password must not be null")
        String password,

        @EnumPattern(name = "gender", regexp = "MALE|FEMALE")
        EGender gender,
        boolean active,
        String photo,
        int day,
        int month,
        int year,
        @EnumPattern(name = "role", regexp = "ROLE_ADMIN|ROLE_INSTRUCTOR|ROLE_STUDENT")
        ERole role
) {
}
