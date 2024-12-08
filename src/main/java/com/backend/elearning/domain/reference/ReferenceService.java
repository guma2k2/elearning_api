package com.backend.elearning.domain.reference;

public interface ReferenceService {

    ReferenceVM create(ReferencePostVM referencePostVM);

    ReferenceVM update(ReferencePostVM referencePostVM, Long referenceId);

    void delete(Long referenceId);

}
