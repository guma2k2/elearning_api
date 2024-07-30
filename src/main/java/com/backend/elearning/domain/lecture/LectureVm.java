package com.backend.elearning.domain.lecture;

import com.backend.elearning.domain.common.Curriculum;
import com.backend.elearning.domain.common.ECurriculumType;
import com.backend.elearning.utils.DateTimeUtils;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LectureVm extends Curriculum {
    private String videoId;

    private String lectureDetails;

    private float duration;

    private String formattedDuration;
    private boolean finished;
    private int watchingSecond = 0;

    public LectureVm(Lecture lecture) {
        super(lecture.getId(), lecture.getTitle(), lecture.getNumber(), ECurriculumType.lecture, lecture.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToMonthYearText(lecture.getUpdatedAt()) : "");
        this.videoId = lecture.getVideoId();
        this.lectureDetails = lecture.getLectureDetails();
        this.duration = lecture.getDuration();
        this.formattedDuration = "";
    }



    public LectureVm(Lecture lecture, boolean finished, int watchingSecond) {
        super(lecture.getId(), lecture.getTitle(), lecture.getNumber(), ECurriculumType.lecture, lecture.getUpdatedAt() != null ?
                DateTimeUtils.convertLocalDateTimeToMonthYearText(lecture.getUpdatedAt()) : "");
        this.videoId = lecture.getVideoId();
        this.lectureDetails = lecture.getLectureDetails();
        this.duration = lecture.getDuration();
        this.formattedDuration = "";
        this.finished = finished;
        this.watchingSecond = watchingSecond;
    }
}
