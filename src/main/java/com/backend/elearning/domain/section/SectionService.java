package com.backend.elearning.domain.section;

public interface SectionService {
    SectionVM create(SectionPostVM sectionPostVM);
    SectionVM getById(Long sectionId);

}
