package com.backend.elearning.domain.promotion;

import com.backend.elearning.utils.DateTimeUtils;

public record PromotionVM(
        Long id,
        String name,
        int discountPercent,
        String startTime,
        String endTime
) {

    public static PromotionVM fromModel(Promotion promotion) {
        String start = promotion.getStartTime() != null ?
                DateTimeUtils.convertLocalDateTimeToString(promotion.getStartTime()) : "";
        String end = promotion.getEndTime() != null ?
                DateTimeUtils.convertLocalDateTimeToString(promotion.getEndTime()) : "";
        return new PromotionVM(promotion.getId(), promotion.getName(), promotion.getDiscountPercent(), start, end);
    }
}
