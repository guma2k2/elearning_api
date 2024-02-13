package com.backend.elearning.domain.common;

public class Curriculum {
    private Long id;
    private String title;

    private float number;

    private ECurriculumType type;

    public Curriculum() {
    }

    public Curriculum(Long id, String title, float number) {
        this.id = id;
        this.title = title;
        this.number = number;
    }

    public Curriculum(Long id, String title, float number, ECurriculumType type) {
        this.id = id;
        this.title = title;
        this.number = number;
        this.type = type;
    }

    public ECurriculumType getType() {
        return type;
    }

    public void setType(ECurriculumType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }
}
