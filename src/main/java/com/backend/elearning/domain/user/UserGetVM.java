package com.backend.elearning.domain.user;

public record UserGetVM(
        Long id,
        String firstName,
        String lastName,
        String avatar
) {
    public static UserGetVM fromModel(User user) {
        return new UserGetVM(user.getId(), user.getFirstName(), user.getLastName(), user.getPhotoId());
    }
}
