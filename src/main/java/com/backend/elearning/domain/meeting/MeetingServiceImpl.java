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
}
