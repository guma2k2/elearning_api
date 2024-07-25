package com.backend.elearning.domain.order.impl;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.coupon.Coupon;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseGetVM;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.order.*;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
    public List<OrderVM> findAllByUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Order> orders = orderRepository.findAllByStudent(email);
        List<OrderVM> orderVMS = orders.stream().map(order -> {
            Long orderId = order.getId();
            Coupon coupon = order.getCoupon();
            AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
            List<OrderDetailVM> orderDetailVMS = orderDetails.stream().map(orderDetail -> {
                Long courseId = orderDetail.getCourse().getId();
                Course course = courseRepository.findById(courseId).orElseThrow();
                CourseGetVM courseVM = CourseGetVM.fromModel(course);
                totalPrice.updateAndGet(v -> v + orderDetail.getPrice());
                return OrderDetailVM.fromModel(orderDetail, courseVM);
            }).toList();
            Double total = totalPrice.get();
            if (coupon != null) {
                total = total - coupon.getDiscountPercent() * total / 100;
            }
            return OrderVM.fromModel(order, orderDetailVMS, total);
        }).toList();
        return orderVMS;
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String orderStatus) {
        EOrderStatus status = EOrderStatus.valueOf(orderStatus);
        orderRepository.updateOrderStatus(orderId, status);
    }



    @Override
    public PageableData<OrderVM> getPageableOrders(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Order> orderPage = orderRepository.findAllCustom(pageable);
        List<Order> orders = orderPage.getContent();
        List<OrderVM> orderVMS = orders.stream().map(order -> {
            Long orderId = order.getId();
            Coupon coupon = order.getCoupon();
            AtomicReference<Double> totalPrice = new AtomicReference<>(0.0);
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
            List<OrderDetailVM> orderDetailVMS = orderDetails.stream().map(orderDetail -> {
                Long courseId = orderDetail.getCourse().getId();
                Course course = courseRepository.findById(courseId).orElseThrow(() -> new  NotFoundException("course not found"));
                CourseGetVM courseVM = CourseGetVM.fromModel(course);
                totalPrice.updateAndGet(v -> v + orderDetail.getPrice());
                return OrderDetailVM.fromModel(orderDetail, courseVM);
            }).toList();
            Double total = totalPrice.get();
            if (coupon != null) {
                total = total - coupon.getDiscountPercent() * total / 100;
            }
            return OrderVM.fromModel(order, orderDetailVMS, total);
        }).toList();
        return new PageableData(
                pageNum,
                pageSize,
                (int) orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderVMS
        );
    }

}
