package com.backend.elearning.domain.order.impl;

import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.course.CourseRepository;
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

    private final CourseRepository courseRepository;
    private static int pageNum = 0;
    private static int pageSize = 15;


    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository, CourseRepository courseRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<CourseListGetVM> getTopCourseBestSeller(int limit) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Long> bestSellerCourses = orderDetailRepository.getBestSellerCourses(pageable);
        List<Long> courseIds = bestSellerCourses.getContent();
        // convert id to list of CourseListGetVM
        List<Course> courses = courseIds.stream().map(aLong -> courseRepository.findById(aLong).orElseThrow()).toList();
        List<CourseListGetVM> courseListGetVMS = courses.stream().map(course -> {
            int totalHours = 1;
            int lectureCount = 11;
            float averageRating = 12 ;
            int ratingCount = 5;
            return CourseListGetVM.fromModel(course, totalHours, lectureCount, averageRating, ratingCount);
        }).toList();
        return courseListGetVMS;
    }
}
