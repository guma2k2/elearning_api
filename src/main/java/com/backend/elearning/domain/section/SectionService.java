package com.backend.elearning.domain.section;

public interface SectionService {
    SectionVM create(SectionPostVM sectionPostVM);
    SectionVM getById(Long sectionId);

    SectionVM update(SectionPostVM sectionPutVM, Long sectionId);
    void delete (Long sectionId);

}
