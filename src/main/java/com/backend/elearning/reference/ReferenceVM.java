package com.backend.elearning.reference;

public record  ReferenceVM (
    Long id,
    String description
) {

    public static ReferenceVM fromModel(Reference reference) {
        return new ReferenceVM(reference.getId(), reference.getDescription());
    }
}
