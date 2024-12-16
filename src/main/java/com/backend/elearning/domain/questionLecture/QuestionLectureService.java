package com.backend.elearning.domain.questionLecture;

import java.util.List;

public interface QuestionLectureService {

    QuestionLectureVM create(QuestionLecturePostVM questionLecturePostVM);



    List<QuestionLectureGetVM> getByLectureId(Long lectureId);

}
