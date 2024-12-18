package com.backend.elearning.domain.questionLecture;

import java.util.List;

public interface QuestionLectureService {

    QuestionLectureVM create(QuestionLecturePostVM questionLecturePostVM);

    QuestionLectureVM update(QuestionLecturePostVM questionLecturePostVM, Long questionLectureId);

    void delete(Long questionLectureId);

    List<QuestionLectureGetVM> getByLectureId(Long lectureId);

    List<QuestionLectureGetVM> getByCourse(Long courseId);

    List<QuestionLectureGetVM> getBySection(Long sectionId);

}
