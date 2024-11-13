package com.backend.elearning.reference;

import com.backend.elearning.domain.classroom.Classroom;
import com.backend.elearning.domain.classroom.ClassroomRepository;
import org.springframework.stereotype.Service;

@Service
public class ReferenceServiceImpl implements ReferenceService{

    private final ReferenceRepository referenceRepository;

    private final ClassroomRepository classroomRepository;

    public ReferenceServiceImpl(ReferenceRepository referenceRepository, ClassroomRepository classroomRepository) {
        this.referenceRepository = referenceRepository;
        this.classroomRepository = classroomRepository;
    }

    @Override
    public ReferenceVM create(ReferencePostVM referencePostVM) {
        Classroom classroom = classroomRepository.findById(referencePostVM.classroomId()).orElseThrow();
        Reference reference = Reference.builder()
                .description(referencePostVM.description())
                .classroom(classroom)
                .build();
        Reference savedReference = referenceRepository.saveAndFlush(reference);
        return ReferenceVM.fromModel(savedReference);
    }
}
