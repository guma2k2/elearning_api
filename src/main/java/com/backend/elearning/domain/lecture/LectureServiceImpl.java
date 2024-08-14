package com.backend.elearning.domain.lecture;

import com.backend.elearning.domain.common.Curriculum;
import com.backend.elearning.domain.section.Section;
import com.backend.elearning.domain.section.SectionRepository;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;

    private final SectionRepository sectionRepository;

    public LectureServiceImpl(LectureRepository lectureRepository, SectionRepository sectionRepository) {
        this.lectureRepository = lectureRepository;
        this.sectionRepository = sectionRepository;
    }


    @Override
    public LectureVm create(LecturePostVM lecturePostVM) {
        Section section = sectionRepository.findById(lecturePostVM.sectionId()).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.SECTION_NOT_FOUND, lecturePostVM.sectionId()));
        Lecture lecture = Lecture.builder()
                .title(lecturePostVM.title())
                .number(lecturePostVM.number())
                .section(section)
                .build();
        lecture.setCreatedAt(LocalDateTime.now());
        lecture.setUpdatedAt(LocalDateTime.now());
        Lecture savedLecture = lectureRepository.save(lecture);
        LectureVm lectureVm = new LectureVm(savedLecture);
        return lectureVm;
    }

    @Override
    public LectureVm update(LecturePostVM lecturePutVM, Long lectureId) {
        Lecture lecture = lectureRepository.findByIdSection(lectureId).orElseThrow(() ->
                new NotFoundException(Constants.ERROR_CODE.LECTURE_NOT_FOUND, lectureId));
        if (!Objects.equals(lecture.getSection().getId(), lecturePutVM.sectionId())) {
            throw new BadRequestException(Constants.ERROR_CODE.LECTURE_SECTION_NOT_SAME, lecturePutVM.sectionId());
        }
        lecture.setTitle(lecturePutVM.title());
        lecture.setNumber(lecturePutVM.number());
        lecture.setVideoId(lecturePutVM.video());
        lecture.setLectureDetails(lecturePutVM.lectureDetails());
        lecture.setDuration(lecturePutVM.duration());
        Lecture savedLecture = lectureRepository.saveAndFlush(lecture);
        lecture.setUpdatedAt(LocalDateTime.now());
        LectureVm lectureVm = new LectureVm(savedLecture);
        return lectureVm;
    }

    @Override
    @Transactional
    public void delete(Long lectureId) {
        lectureRepository.deleteById(lectureId);
    }
}
