package com.backend.elearning.domain.meeting;


import com.backend.elearning.domain.classroom.ClassroomPostVM;
import com.backend.elearning.domain.classroom.ClassroomVM;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MeetingController {
    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }


    @PostMapping("/meetings")
    public ResponseEntity<MeetingVM> create (
            @RequestBody MeetingPostVM meetingPostVM
    ) {
        MeetingVM meetingVM = meetingService.create(meetingPostVM);
        return ResponseEntity.ok().body(meetingVM);
    }
}
