package com.backend.elearning.domain.section;

import com.backend.elearning.domain.common.Curriculum;

import java.util.List;

public record SectionVM(
        Long id,
        String title,
        float number,
        String objective,
        List<Curriculum> curriculums
) {
    public static SectionVM fromModel(Section section, List<Curriculum> curriculums) {
        return new SectionVM(section.getId(), section.getTitle(), section.getNumber(), section.getObjective(), curriculums);
    }
}
