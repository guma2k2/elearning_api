package com.backend.elearning.domain.section;

import java.util.List;

public interface SectionService {
    SectionVM create(SectionPostVM sectionPostVM);
    SectionVM getById(Long sectionId, String email);

    SectionVM update(SectionPostVM sectionPutVM, Long sectionId);
    void delete (Long sectionId);

    List<Section> findByCourseId(Long courseId);

}
