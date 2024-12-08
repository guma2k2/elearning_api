package com.backend.elearning.domain.reference;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/references/{referenceId}")
    public ResponseEntity<ReferenceVM> update (
            @RequestBody ReferencePostVM referencePostVM,
            Long referenceId
    ) {
        ReferenceVM referenceVM = referenceService.update(referencePostVM, referenceId);
        return ResponseEntity.ok().body(referenceVM);
    }

    @DeleteMapping("/references/{referenceId}")
    public ResponseEntity<String> delete (
          @PathVariable("referenceId")  Long referenceId
    ) {
        referenceService.delete(referenceId);
        return ResponseEntity.ok().body("deleted ");
    }
}
