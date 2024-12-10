package com.backend.elearning.domain.promotion;

import java.time.LocalDateTime;

public record PromotionPostVM (
        String name,
        int discountPercent,
        String startTime,
        String endTime
) {
}
