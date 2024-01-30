package com.backend.elearning.domain.user;


public record UserVm (
         Long id,
         String email,

         String firstName,
         String lastName,
         String gender,
         boolean active,
         String photoURL,
         String dateOfBirth,
         String role
) {
    public static UserVm fromModel (User user, String photoUrl) {
        return new UserVm(user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender().name(),
                user.isActive(),
                photoUrl,
                user.getDateOfBirth().toString(),
                user.getRole().name());
    }
}
