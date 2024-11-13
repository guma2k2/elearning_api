package com.backend.elearning.domain.referencefile;

import com.backend.elearning.domain.meeting.MeetingPostVM;
import com.backend.elearning.domain.meeting.MeetingVM;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ReferenceFileController {

    private final ReferenceFileService referenceFileService;

    public ReferenceFileController(ReferenceFileService referenceFileService) {
        this.referenceFileService = referenceFileService;
    }

    @PostMapping("/referencefiles")
    public ResponseEntity<ReferenceFileVM> create (
            @RequestBody ReferenceFilePostVM referenceFilePostVM
    ) {
        ReferenceFileVM referenceFileVM = referenceFileService.create(referenceFilePostVM);
        return ResponseEntity.ok().body(referenceFileVM);
    }
}
