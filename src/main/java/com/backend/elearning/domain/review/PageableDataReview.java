package com.backend.elearning.domain.review;

import com.backend.elearning.domain.common.PageableData;

import java.util.List;

public class PageableDataReview<T> extends PageableData<T> {
    int percentFiveStar;
    int percentFourStar;
    int percentThreeStar;
    int percentTwoStar;
    int percentOneStar;

    public PageableDataReview(int percentFiveStar, int percentFourStar, int percentThreeStar, int percentTwoStar, int percentOneStar) {
        this.percentFiveStar = percentFiveStar;
        this.percentFourStar = percentFourStar;
        this.percentThreeStar = percentThreeStar;
        this.percentTwoStar = percentTwoStar;
        this.percentOneStar = percentOneStar;
    }

    public PageableDataReview(int pageNum, int pageSize, long totalElements, int totalPages, List<T> content, int percentFiveStar, int percentFourStar, int percentThreeStar, int percentTwoStar, int percentOneStar) {
        super(pageNum, pageSize, totalElements, totalPages, content);
        this.percentFiveStar = percentFiveStar;
        this.percentFourStar = percentFourStar;
        this.percentThreeStar = percentThreeStar;
        this.percentTwoStar = percentTwoStar;
        this.percentOneStar = percentOneStar;
    }

    public int getPercentFiveStar() {
        return percentFiveStar;
    }

    public void setPercentFiveStar(int percentFiveStar) {
        this.percentFiveStar = percentFiveStar;
    }

    public int getPercentFourStar() {
        return percentFourStar;
    }

    public void setPercentFourStar(int percentFourStar) {
        this.percentFourStar = percentFourStar;
    }

    public int getPercentThreeStar() {
        return percentThreeStar;
    }

    public void setPercentThreeStar(int percentThreeStar) {
        this.percentThreeStar = percentThreeStar;
    }

    public int getPercentTwoStar() {
        return percentTwoStar;
    }

    public void setPercentTwoStar(int percentTwoStar) {
        this.percentTwoStar = percentTwoStar;
    }

    public int getPercentOneStar() {
        return percentOneStar;
    }

    public void setPercentOneStar(int percentOneStar) {
        this.percentOneStar = percentOneStar;
    }
}