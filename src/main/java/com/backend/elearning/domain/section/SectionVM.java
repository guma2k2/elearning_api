package com.backend.elearning.domain.section;

import com.backend.elearning.domain.common.Curriculum;

import java.util.List;

public record SectionVM(
        Long id,
        String title,

        int number,

        String objective,
        List<Curriculum> curriculums
) {
}
