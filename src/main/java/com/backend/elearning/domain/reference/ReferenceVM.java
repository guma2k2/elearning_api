package com.backend.elearning.domain.reference;

public record  ReferenceVM (
    Long id,
    String description
) {

    public static ReferenceVM fromModel(Reference reference) {
        return new ReferenceVM(reference.getId(), reference.getDescription());
    }
}
