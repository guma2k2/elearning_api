package com.backend.elearning.domain.referencefile;


import java.util.List;

public interface ReferenceFileService {
    ReferenceFileVM create(ReferenceFilePostVM referenceFilePostVM);
    List<ReferenceFileVM> getByReferenceId(Long referenceId);

    void delete(Long referenceFileId);
}
