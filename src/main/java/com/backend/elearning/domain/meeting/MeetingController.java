package com.backend.elearning.domain.meeting;


import com.backend.elearning.domain.classroom.ClassroomPostVM;
import com.backend.elearning.domain.classroom.ClassroomVM;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/meetings/{meetingId}")
    public ResponseEntity<MeetingVM> update (
            @RequestBody MeetingPostVM meetingPostVM,
            @PathVariable("meetingId") Long meetingId
    ) {
        MeetingVM meetingVM = meetingService.update(meetingPostVM, meetingId);
        return ResponseEntity.ok().body(meetingVM);
    }

    @DeleteMapping("/meetings/{meetingId}")
    public ResponseEntity<String> delete (
            @PathVariable("meetingId") Long meetingId
    ) {
        meetingService.delete( meetingId);
        return ResponseEntity.ok().body("deleted");
    }
}
