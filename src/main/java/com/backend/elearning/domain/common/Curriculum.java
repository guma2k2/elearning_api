package com.backend.elearning.domain.common;

public class Curriculum {
    private Long id;
    private String title;

    private int number;

    public Curriculum() {
    }

    public Curriculum(Long id, String title, int number) {
        this.id = id;
        this.title = title;
        this.number = number;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
