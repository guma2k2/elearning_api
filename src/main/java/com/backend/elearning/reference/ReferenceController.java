package com.backend.elearning.reference;

import com.backend.elearning.domain.meeting.MeetingPostVM;
import com.backend.elearning.domain.meeting.MeetingService;
import com.backend.elearning.domain.meeting.MeetingVM;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ReferenceController {

    private final ReferenceService referenceService;

    public ReferenceController(ReferenceService referenceService) {
        this.referenceService = referenceService;
    }

    @PostMapping("/references")
    public ResponseEntity<ReferenceVM> create (
            @RequestBody ReferencePostVM referencePostVM
    ) {
        ReferenceVM referenceVM = referenceService.create(referencePostVM);
        return ResponseEntity.ok().body(referenceVM);
    }
}
