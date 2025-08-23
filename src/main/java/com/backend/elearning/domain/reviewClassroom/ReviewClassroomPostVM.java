package com.backend.elearning.domain.reviewClassroom;

import jakarta.validation.constraints.NotNull;

public record ReviewClassroomPostVM(Long classroomId,
                                    String content,
                                    @NotNull(message = "rating star must not be null")
                                    Integer ratingStar
) {
}
