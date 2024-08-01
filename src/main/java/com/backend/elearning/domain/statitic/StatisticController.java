package com.backend.elearning.domain.statitic;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class StatisticController {

    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/statistic/time")
    public ResponseEntity<List<StatisticTime>> getByYear(@RequestParam("year") int year,
                                                         @RequestParam(value = "month", required = false) Integer month
    ) {
        if (month != null) {
            List<StatisticTime> statistics = statisticService.findByMonth(month, year);
            return ResponseEntity.ok().body(statistics);
        }
        List<StatisticTime> statistics = statisticService.findByYear(year);
        return ResponseEntity.ok().body(statistics);
    }


    @GetMapping("/statistic/dashboard")
    public ResponseEntity<Dashboard> getDashboard() {
        Dashboard dashboard = statisticService.getDashboard();
        return ResponseEntity.ok().body(dashboard);
    }

    @GetMapping("/statistic/course")
    public ResponseEntity<List<StatisticCourse>> getStatisticProductByTime(
            @RequestParam("from") String from,
            @RequestParam("to") String to
    ) {

        List<StatisticCourse> statisticCourses = statisticService.getByTime(from, to);
        return ResponseEntity.ok().body(statisticCourses);
    }

}
