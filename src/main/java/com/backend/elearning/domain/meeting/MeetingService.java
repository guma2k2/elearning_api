package com.backend.elearning.domain.meeting;

public interface MeetingService {

    MeetingVM create(MeetingPostVM meetingPostVM);

    MeetingVM update(MeetingPostVM meetingPostVM, Long meetingId);

    void delete(Long meetingId);

}
