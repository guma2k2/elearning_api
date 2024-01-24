package com.backend.elearning.domain.user;


public record UserVm (
         Long id,
         String email,

         String firstName,
         String lastName,
         String gender,
         boolean active,
         String photoId,
         String dateOfBirth,
         String role
) {
    public static UserVm fromModel (User user) {
        return new UserVm(user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender().name(),
                user.isActive(),
                user.getPhotoId(),
                user.getDateOfBirth().toString(),
                user.getRole().name());
    }
}
