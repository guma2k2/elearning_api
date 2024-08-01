package com.backend.elearning.domain.statitic;

import java.util.Objects;

public class StatisticTime {

    private String name ;
    private long total;

    public StatisticTime() {
    }

    public StatisticTime(String name) {
        this.name = name;
    }

    public StatisticTime(String name, long total) {
        this.name = name;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatisticTime that = (StatisticTime) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


}
