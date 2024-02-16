package com.backend.elearning.domain.lecture;

import com.backend.elearning.domain.common.Curriculum;
import com.backend.elearning.domain.common.ECurriculumType;

public class LectureVm extends Curriculum {
    private String videoId;

    private String lectureDetails;

    private float duration;

    public LectureVm(String videoId, String lectureDetails, int duration) {
        this.videoId = videoId;
        this.lectureDetails = lectureDetails;
        this.duration = duration;
    }
    public LectureVm(Long id, String title, float number, ECurriculumType type, String videoId, String lectureDetails, float duration) {
        super(id, title, number, type);
        this.videoId = videoId;
        this.lectureDetails = lectureDetails;
        this.duration = duration;
    }

    public LectureVm(Lecture lecture) {
        super(lecture.getId(), lecture.getTitle(), lecture.getNumber(), ECurriculumType.lecture);
        this.videoId = "";
        this.lectureDetails = "";
        this.duration = 0;
    }


    public LectureVm() {

    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getLectureDetails() {
        return lectureDetails;
    }

    public void setLectureDetails(String lectureDetails) {
        this.lectureDetails = lectureDetails;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}
