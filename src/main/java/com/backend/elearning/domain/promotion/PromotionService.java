package com.backend.elearning.domain.promotion;

import com.backend.elearning.domain.common.PageableData;

public interface PromotionService {

    PromotionVM create(PromotionPostVM promotionPostVM);
    PromotionVM update(PromotionPostVM promotionPostVM, Long id);
    void addCourseToPromotion (Long promotionId, Long courseId);
    void removeCourseFromPromotion(Long promotionId, Long courseId);
    void deletePromotion(Long promotionId);

    PageableData<PromotionVM> getPageablePromotions(int pageNum, int pageSize);
}
