package com.backend.elearning.domain.statitic;

import com.backend.elearning.domain.review.ReviewPostVM;
import com.backend.elearning.domain.review.ReviewVM;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class StatisticController {


    @GetMapping("/statistic/year/{year}")
    public ResponseEntity<Void> getByYear(@PathVariable("year") int year){
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/statistic/month/{month}")
    public ResponseEntity<Void> getByMonth(@PathVariable("month") int month){
        return ResponseEntity.noContent().build();
    }
}
