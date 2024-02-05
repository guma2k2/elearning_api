package com.backend.elearning.domain.lecture;

import com.backend.elearning.domain.section.Section;
import com.backend.elearning.domain.section.SectionRepository;
import org.springframework.stereotype.Service;

@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;

    private final SectionRepository sectionRepository;

    public LectureServiceImpl(LectureRepository lectureRepository, SectionRepository sectionRepository) {
        this.lectureRepository = lectureRepository;
        this.sectionRepository = sectionRepository;
    }


    @Override
    public Lecture create(LecturePostVM lecturePostVM) {
        Section section = sectionRepository.findById(lecturePostVM.sectionId()).orElseThrow();
        Lecture lecture = Lecture.builder()
                .title(lecturePostVM.title())
                .lectureDetails(lecturePostVM.lectureDetails())
                .videoId(lecturePostVM.videoId())
                .number(lecturePostVM.number())
                .section(section)
                .build();
        return lectureRepository.save(lecture);
    }
}
