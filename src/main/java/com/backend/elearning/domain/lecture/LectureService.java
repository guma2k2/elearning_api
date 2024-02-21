package com.backend.elearning.domain.lecture;

public interface LectureService {
    LectureVm create(LecturePostVM lecturePostVM);
    LectureVm update(LecturePostVM lecturePutVM, Long lectureId);

    void delete(Long lectureId);
}
