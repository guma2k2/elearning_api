package com.backend.elearning.domain.user;

public enum ERole {
    ROLE_ADMIN ("all permission"), ROLE_INSTRUCTOR(""), ROLE_CUSTOMER("");

    String value;

    ERole(String value) {
        this.value = value;
    }
}
