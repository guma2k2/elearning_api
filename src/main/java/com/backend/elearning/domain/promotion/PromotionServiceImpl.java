package com.backend.elearning.domain.promotion;


import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.coupon.Coupon;
import com.backend.elearning.domain.coupon.CouponVM;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.exception.BadRequestException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.DateTimeUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PromotionServiceImpl implements PromotionService{


    private final PromotionRepository promotionRepository;
    private final CourseRepository courseRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository, CourseRepository courseRepository) {
        this.promotionRepository = promotionRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public PromotionVM create(PromotionPostVM promotionPostVM) {
        LocalDateTime startTime = DateTimeUtils.convertStringToLocalDateTime(promotionPostVM.startTime(), DateTimeUtils.NORMAL_TYPE);
        LocalDateTime endTime = DateTimeUtils.convertStringToLocalDateTime(promotionPostVM.endTime(), DateTimeUtils.NORMAL_TYPE);

        if (startTime.isAfter(endTime)) {
            throw new BadRequestException("Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc");
        }
        Promotion promotion = Promotion.builder()
                .discountPercent(promotionPostVM.discountPercent())
                .name(promotionPostVM.name())
                .startTime(startTime)
                .endTime(endTime)
                .build();

        Promotion savedPromotion = promotionRepository.saveAndFlush(promotion);
        return PromotionVM.fromModel(savedPromotion);
    }

    @Override
    public PromotionVM update(PromotionPostVM promotionPostVM, Long id) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy khuyến mãi"));
        LocalDateTime startTime = DateTimeUtils.convertStringToLocalDateTime(promotionPostVM.startTime(), DateTimeUtils.NORMAL_TYPE);
        LocalDateTime endTime = DateTimeUtils.convertStringToLocalDateTime(promotionPostVM.endTime(), DateTimeUtils.NORMAL_TYPE);

        if (startTime.isAfter(endTime)) {
            throw new BadRequestException("Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc");
        }
        promotion.setName(promotionPostVM.name());
        promotion.setStartTime(startTime);
        promotion.setEndTime(endTime);
        promotion.setDiscountPercent(promotionPostVM.discountPercent());
        Promotion savedPromotion = promotionRepository.saveAndFlush(promotion);
        return PromotionVM.fromModel(savedPromotion);
    }

    @Override
    public void addCourseToPromotion(Long promotionId, Long courseId) {
        Promotion newPromotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new NotFoundException("Promotion not found"));

        // Fetch the course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        course = courseRepository.findByIdWithPromotions(course)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        LocalDateTime now = LocalDateTime.now();
//        if (now.isBefore(newPromotion.getEndTime()) || now.isAfter(newPromotion.getStartTime())) {
//            throw new BadRequestException("Cannot add course to promotion outside the valid time range.");
//        }

        // Check if the course is already in a conflicting promotion
        Set<Promotion> currentPromotions = course.getPromotions();
        for (Promotion existingPromotion : currentPromotions) {
            if (isTimeOverlapping(existingPromotion.getStartTime(), existingPromotion.getEndTime(),
                    newPromotion.getStartTime(), newPromotion.getEndTime())) {
                throw new BadRequestException("Course is already part of a conflicting promotion.");
            }
        }

        // Add the course to the new promotion
        newPromotion.getCourses().add(course);
        course.getPromotions().add(newPromotion);

        // Save the changes
        promotionRepository.save(newPromotion);
    }

    private boolean isTimeOverlapping(LocalDateTime start1, LocalDateTime end1,
                                      LocalDateTime start2, LocalDateTime end2) {
        return !(end1.isEqual(start2) || end2.isEqual(start1)) // Exclude boundary overlap
                && (start1.isBefore(end2) && start2.isBefore(end1));
    }
    @Override
    @Transactional
    public void removeCourseFromPromotion(Long promotionId, Long courseId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new NotFoundException("Promotion not found"));

        // Fetch the course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        // Remove the course from the promotion's list of courses
        promotion.getCourses().remove(course);

        // Optionally, remove the promotion from the course's list of promotions
        course.getPromotions().remove(promotion);

        // Save the promotion (cascade will update the course as well if configured)
        promotionRepository.save(promotion);
    }

    @Override
    public void deletePromotion(Long promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new NotFoundException("Promotion not found"));
        if (promotion.getCourses().size() > 0) {
            throw new BadRequestException("This promotion had courses");
        }
        promotionRepository.deleteById(promotionId);
    }

    @Override
    public PageableData<PromotionVM> getPageablePromotions(int pageNum, int pageSize) {
        List<PromotionVM> promotionVMS = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        Page<Promotion> promotionPage = promotionRepository.findAll(pageable);
        List<Promotion> promotions = promotionPage.getContent();
        for (Promotion promotion : promotions) {
            promotionVMS.add(PromotionVM.fromModel(promotion));
        }
        return new PageableData(
                pageNum,
                pageSize,
                (int) promotionPage.getTotalElements(),
                promotionPage.getTotalPages(),
                promotionVMS
        );
    }
}
