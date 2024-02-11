package com.backend.elearning.domain.section;

import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SectionServiceImpl implements SectionService{
    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;

    public SectionServiceImpl(SectionRepository sectionRepository, CourseRepository courseRepository) {
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public SectionVM create(SectionPostVM sectionPostVM) {
        Course course = courseRepository.findById(sectionPostVM.courseId()).orElseThrow();
        Section section = Section.builder()
                .title(sectionPostVM.title())
                .number(sectionPostVM.number())
                .objective(sectionPostVM.objective())
                .course(course)
                .build();
        return SectionVM.fromModel(sectionRepository.save(section), new ArrayList<>());
    }
}
