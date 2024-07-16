package com.backend.elearning.domain.order.impl;

import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseGetVM;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.order.*;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository  courseRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Long createOrder(OrderPostDto orderPostDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByEmail( email).orElseThrow();
        Order order = Order.builder()
                .status(EOrderStatus.PENDING)
                .student(student)
                .createdAt(LocalDateTime.now())
                .build();
        Order savedOrder = orderRepository.saveAndFlush(order);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailPostDto orderPostDetail: orderPostDto.orderDetails()) {
            Long courseId = orderPostDetail.courseId();
            Course course = courseRepository.findById(courseId).orElseThrow();
            OrderDetail orderDetail = OrderDetail
                    .builder()
                    .order(savedOrder)
                    .course(course)
                    .price(orderPostDetail.price())
                    .build();
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        return savedOrder.getId();
    }

    @Override
    public List<OrderDto> findAllByUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Order> orders = orderRepository.findAllByStudent(email);
        List<OrderDto> orderDtos = orders.stream().map(order -> {
            List<OrderDetailDto> orderDetailDtos = order.getOrderDetails().stream().map(orderDetail -> {
                Long courseId = orderDetail.getId();
                Course course = courseRepository.findById(courseId).orElseThrow();
                CourseGetVM courseVM = CourseGetVM.fromModel(course);
                return OrderDetailDto.fromModel(orderDetail, courseVM);
            }).toList();
            return OrderDto.fromModel(order, orderDetailDtos);
        }).toList();
        return orderDtos;
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String orderStatus) {
        EOrderStatus status = EOrderStatus.valueOf(orderStatus);
        orderRepository.updateOrderStatus(orderId, status);
    }

    @Override
    public List<OrderGetListDto> findAll()  {
        return null;
    }

    private final LocalDateTime convertStringToLocalDateTimeOfPayDay (String payday) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime localDateTime = LocalDateTime.parse(payday, formatter);
        return localDateTime;
    }
}
