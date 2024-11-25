package com.backend.elearning.domain.referencefile;

import com.backend.elearning.domain.meeting.MeetingPostVM;
import com.backend.elearning.domain.meeting.MeetingVM;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @DeleteMapping("/referencefiles/{id}")
    public ResponseEntity<String> delete (
            @PathVariable("id") Long referenceFileId
    ) {
        referenceFileService.delete(referenceFileId);
        return ResponseEntity.ok().body("deleted");
    }
}
