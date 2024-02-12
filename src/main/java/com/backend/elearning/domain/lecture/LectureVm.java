package com.backend.elearning.domain.lecture;

import com.backend.elearning.domain.common.Curriculum;

public class LectureVm extends Curriculum {
    private String videoId;

    private String lectureDetails;

    private int duration;

    public LectureVm(String videoId, String lectureDetails, int duration) {
        this.videoId = videoId;
        this.lectureDetails = lectureDetails;
        this.duration = duration;
    }

    public LectureVm(Long id, String title, float number, String videoId, String lectureDetails, int duration) {
        super(id, title, number);
        this.videoId = videoId;
        this.lectureDetails = lectureDetails;
        this.duration = duration;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
