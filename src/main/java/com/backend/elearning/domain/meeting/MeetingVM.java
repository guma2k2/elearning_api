package com.backend.elearning.domain.meeting;

import com.backend.elearning.utils.DateTimeUtils;

public record MeetingVM (
        Long id,
        String code,
        String startTime,
        String endTime
) {
    public static MeetingVM fromModel(Meeting meeting) {
        String start = meeting.getStartTime() != null ?
                DateTimeUtils.convertLocalDateTimeToString(meeting.getStartTime()) : "";
        String end = meeting.getEndTime() != null ?
                DateTimeUtils.convertLocalDateTimeToString(meeting.getEndTime()) : "";
        return new MeetingVM(meeting.getId(), meeting.getCode(), start, end );
    }
}
