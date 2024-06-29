package com.backend.elearning.domain.auth;

import com.backend.elearning.domain.user.UserVm;

public record AuthenticationVm  (
        String token,
        UserVm user
) {
}
