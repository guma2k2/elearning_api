package com.backend.elearning.domain.user;

public record UserGetDetailVm (Long id,
                               String email,

                               String firstName,
                               String lastName,
                               String gender,
                               boolean active,
                               String photoURL,
                               String dateOfBirth,
                               String role) {
}
