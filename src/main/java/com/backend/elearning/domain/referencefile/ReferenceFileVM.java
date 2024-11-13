package com.backend.elearning.domain.referencefile;

public record ReferenceFileVM(
        Long id,
        String fileName,
        String fileUrl
) {

    public static ReferenceFileVM fromModel(ReferenceFile referenceFile) {
        return new ReferenceFileVM(referenceFile.getId(), referenceFile.getFileName(), referenceFile.getFileUrl());
    }
}
