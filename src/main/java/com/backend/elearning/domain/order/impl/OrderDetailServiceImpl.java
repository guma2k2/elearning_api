package com.backend.elearning.domain.order.impl;

import com.backend.elearning.domain.course.*;
import com.backend.elearning.domain.order.OrderDetailRepository;
import com.backend.elearning.domain.order.OrderDetailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    private static int pageNum = 0;
    private static int pageSize = 15;
    private final CourseService courseService;
    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository, CourseService courseService) {
        this.orderDetailRepository = orderDetailRepository;
        this.courseService = courseService;
    }

    @Override
    public List<CourseListGetVM> getTopCourseBestSeller(int limit) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Long> bestSellerCourses = orderDetailRepository.getBestSellerCourses(pageable);
        List<Long> courseIds = bestSellerCourses.getContent();
        // convert id to list of CourseListGetVM
        List<CourseListGetVM> courseListGetVMS = courseIds.stream().map(id -> {
            return courseService.getCourseListGetVMById(id);
        }).toList();
        return courseListGetVMS;
    }
}
