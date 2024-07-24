package com.backend.elearning.domain.statitic;


import com.backend.elearning.domain.payment.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticService {

    private final PaymentRepository paymentRepository;

    private static final String MONTH_PATTERN = "Th %d";

    private static final String DAY_PATTERN = "Day %d";

    public StatisticService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    public List<StatisticTime> findByYear(int year) {
        int numberMonthOfYear = 12;
        List<StatisticTime> statisticTimes = new ArrayList<>();
        List<Statistic> statistics = paymentRepository.findByYear(year);
        for (Statistic statistic: statistics) {
            String name = String.format(MONTH_PATTERN, statistic.getTime());
            Long total = statistic.getTotal();
            StatisticTime statisticTime = new StatisticTime(name, total);
            statisticTimes.add(statisticTime);
        }
        for (int month = 1 ; month <= numberMonthOfYear; month++) {
            String name = String.format(MONTH_PATTERN, month);
            StatisticTime statisticTime = new StatisticTime(name);
            if (!statisticTimes.contains(statisticTime)) {
                statisticTimes.add(new StatisticTime(name, 0L));
            }
        }
        return statisticTimes;
    }


    public List<StatisticTime> findByMonth(int month, int year) {
//        int numberMonthOfYear = 12;
        int numberOfDayInMonth = getNumberOfDaysInMonth(month, year);
        List<StatisticTime> statisticTimes = new ArrayList<>();
        List<Statistic> statistics = paymentRepository.findByMonthAndYear(month, year);
        for (Statistic statistic: statistics) {
            String name = String.format(DAY_PATTERN, statistic.getTime());
            Long total = statistic.getTotal();
            StatisticTime statisticTime = new StatisticTime(name, total);
            statisticTimes.add(statisticTime);
        }
        for (int day = 1 ; day <= numberOfDayInMonth; day++) {
            String name = String.format(DAY_PATTERN, month);
            StatisticTime statisticTime = new StatisticTime(name);
            if (!statisticTimes.contains(statisticTime)) {
                statisticTimes.add(new StatisticTime(name, 0L));
            }
        }
        return statisticTimes;
    }

    public int getNumberOfDaysInMonth(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.lengthOfMonth();
    }
}
