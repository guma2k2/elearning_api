package com.backend.elearning.domain.statitic;

public class Statistic {
   private int time;
   private long total;

   public Statistic(int time, Long total) {
      this.time = time;
      this.total = total;
   }
   public Statistic() {
   }

   public int getTime() {
      return time;
   }

   public void setTime(int time) {
      this.time = time;
   }

   public long getTotal() {
      return total;
   }

   public void setTotal(long total) {
      this.total = total;
   }
}
