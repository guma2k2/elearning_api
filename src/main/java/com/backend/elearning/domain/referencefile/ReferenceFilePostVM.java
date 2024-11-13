package com.backend.elearning.domain.referencefile;

public record ReferenceFilePostVM(
        String fileName,
        String fileUrl,
        Long referenceId
) {
}
