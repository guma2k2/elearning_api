package com.backend.elearning.domain.user;

import com.backend.elearning.utils.EnumPattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserPutVm(
    Long id,
    @Email(message = "invalid format")
    String email,
    @NotBlank(message = "first name must not be blank")
    String firstName,

    @NotBlank(message = "last name must not be blank")
    String lastName,
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
) {}
