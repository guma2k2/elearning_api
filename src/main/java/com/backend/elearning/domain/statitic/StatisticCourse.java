package com.backend.elearning.domain.statitic;

public class StatisticCourse {
    private String course;
    private Long quantity;
    private Long price;

    public StatisticCourse(String course, Long quantity, Long price) {
        this.course = course;
        this.quantity = quantity;
        this.price = price;
    }

    public StatisticCourse() {
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
