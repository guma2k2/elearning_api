package com.backend.elearning.domain.student;

public record StudentPutVM(
        Long id,
        String email,
        String firstName,
        String lastName,
        String password,
        String gender,
        String photo,
        Integer day,
        Integer month,
        Integer year
) {
}
