package com.backend.elearning.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserPutVm(
    Long id,
    @Email(message = "email invalid format")
    String email,

    @NotBlank(message = "first name must not be blank")
    String firstName,

    @NotBlank(message = "last name must not be blank")
    String lastName,

    @Min(message = "password need at least 6 character", value = 6)
    String password,
    String gender,
    boolean active,
    String photo,
    @Length(min = 1, max = 31)
    int day,
    @Length(min = 1, max = 12)
    int month,
    int year,
    String role
) {}
