package com.backend.elearning.domain.meeting;

public record MeetingPostVM (
        String code,
        String startTime,
        String endTime,
        Long classroomId
) {
}
