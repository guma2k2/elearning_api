package com.backend.elearning.domain.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record StudentPutVM(
        Long id,
        @Email(message = "email invalid format")
        String email,
        @NotBlank(message = "first name must not be blank")
        String firstName,
        @NotBlank(message = "last name must not be blank")
        String lastName,
        String password,
        String gender,
        String photo,
        Integer day,
        Integer month,
        Integer year
) {
}
