package com.backend.elearning.domain.meeting;


import com.backend.elearning.domain.classroom.Classroom;
import com.backend.elearning.domain.classroom.ClassroomRepository;
import com.backend.elearning.utils.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MeetingServiceImpl implements MeetingService{

    private final MeetingRepository meetingRepository;
    private final ClassroomRepository classroomRepository;

    public MeetingServiceImpl(MeetingRepository meetingRepository, ClassroomRepository classroomRepository) {
        this.meetingRepository = meetingRepository;
        this.classroomRepository = classroomRepository;
    }

    @Override
    public MeetingVM create(MeetingPostVM meetingPostVM) {
        Classroom classroom = classroomRepository.findById(meetingPostVM.classroomId()).orElseThrow();
        LocalDateTime startTime = DateTimeUtils.convertStringToLocalDateTime(meetingPostVM.startTime(), DateTimeUtils.NORMAL_TYPE);
        LocalDateTime endTime = DateTimeUtils.convertStringToLocalDateTime(meetingPostVM.endTime(), DateTimeUtils.NORMAL_TYPE);
        Meeting meeting = Meeting.builder()
                .code(meetingPostVM.code())
                .startTime(startTime)
                .endTime(endTime)
                .classroom(classroom)
                .build();
        Meeting savedMeeting = meetingRepository.saveAndFlush(meeting);
        return MeetingVM.fromModel(savedMeeting);
    }

    @Override
    public MeetingVM update(MeetingPostVM meetingPostVM, Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow();
        LocalDateTime startTime = DateTimeUtils.convertStringToLocalDateTime(meetingPostVM.startTime(), DateTimeUtils.NORMAL_TYPE);
        LocalDateTime endTime = DateTimeUtils.convertStringToLocalDateTime(meetingPostVM.endTime(), DateTimeUtils.NORMAL_TYPE);
        meeting.setCode(meetingPostVM.code());
        meeting.setStartTime(startTime);
        meeting.setEndTime(endTime);
        Meeting updatedMeeting = meetingRepository.saveAndFlush(meeting);
        return MeetingVM.fromModel(updatedMeeting);
    }

    @Override
    public void delete(Long meetingId) {
        meetingRepository.deleteById(meetingId);
    }
}
