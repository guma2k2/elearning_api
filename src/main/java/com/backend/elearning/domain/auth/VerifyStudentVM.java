package com.backend.elearning.domain.auth;

public record VerifyStudentVM (String email, String verificationCode, String type) {
}
