package com.backend.elearning.domain.statitic;

public class StatisticCourse {
    private String course;
    private Long quantity;
    private Double price;

    public StatisticCourse() {
    }

    public StatisticCourse(String course, Long quantity, Double price) {
        this.course = course;
        this.quantity = quantity;
        this.price = price;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
