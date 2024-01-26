package com.backend.elearning.domain.user;

public record UserPostVm(
        String email,
        String firstName,
        String lastName,
        String password,
        String gender,
        boolean active,
        String photoId,
        int day,
        int month,
        int year,
        String role
) {
}
