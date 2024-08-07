package com.backend.elearning.domain.user;

public record UserPutVm(
    Long id,
    String email,
    String firstName,
    String lastName,
    String password,
    String gender,
    boolean active,
    String photo,
    int day,
    int month,
    int year,
    String role
) {}
