package com.backend.elearning.domain.order.impl;

import com.backend.elearning.domain.cart.Cart;
import com.backend.elearning.domain.cart.CartRepository;
import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.coupon.Coupon;
import com.backend.elearning.domain.coupon.CouponRepository;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseGetVM;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.course.CourseStatus;
import com.backend.elearning.domain.order.*;
import com.backend.elearning.domain.review.Review;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.domain.user.UserRepository;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import com.backend.elearning.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository  courseRepository;
    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;


    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, StudentRepository studentRepository, CourseRepository courseRepository, CouponRepository couponRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.couponRepository = couponRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional
    public Long createOrder(OrderPostDto orderPostDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("received orderPostDto: {}", orderPostDto);
        log.info("received email from token: {}", email);
        Coupon coupon = orderPostDto.couponCode() != null ? couponRepository.findByCode(orderPostDto.couponCode()).orElseThrow() : null;
        Student student = studentRepository.findByEmail( email).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.STUDENT_NOT_FOUND, email));
        Order order = Order.builder()
                .status(EOrderStatus.PENDING)
                .student(student)
                .createdAt(LocalDateTime.now())
                .build();
        if (coupon != null) {
            order.setCoupon(coupon);
        }
        Order savedOrder = orderRepository.saveAndFlush(order);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailPostDto orderPostDetail: orderPostDto.orderDetails()) {
            Long courseId = orderPostDetail.courseId();
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.COURSE_NOT_FOUND, courseId));
            OrderDetail orderDetail = OrderDetail
                    .builder()
                    .order(savedOrder)
                    .course(course)
                    .price(orderPostDetail.price())
                    .build();
            orderDetails.add(orderDetail);
            List<Cart> carts = cartRepository.findByUserEmailWithBuyLater(email);
            if (carts.size() > 0) {
                for (Cart cart: carts) {
                    cartRepository.delete(cart);
                }
            }
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
            AtomicReference<Long> totalPrice = new AtomicReference<>(0L);
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
            List<OrderDetailVM> orderDetailVMS = orderDetails.stream().map(orderDetail -> {
                Long courseId = orderDetail.getCourse().getId();
                Course course = courseRepository.findById(courseId).orElseThrow();
                CourseGetVM courseVM = CourseGetVM.fromModel(course);
                totalPrice.updateAndGet(v -> v + orderDetail.getPrice());
                return OrderDetailVM.fromModel(orderDetail, courseVM);
            }).toList();
            Long total = totalPrice.get();
            if (coupon != null) {
                total = total - coupon.getDiscountPercent() * total / 100;
            }
            return OrderVM.fromModel(order, orderDetailVMS, total);
        }).toList();
        return orderVMS;
    }

    @Override
    public List<OrderVM> findAllByUserIdAndStatus(EOrderStatus status) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Order> orders = orderRepository.findAllByStudentAndStatus(email, status);
        List<OrderVM> orderVMS = orders.stream().map(order -> {
            Long orderId = order.getId();
            Coupon coupon = order.getCoupon();
            AtomicReference<Long> totalPrice = new AtomicReference<>(0L);
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
            List<OrderDetailVM> orderDetailVMS = orderDetails.stream().map(orderDetail -> {
                Long courseId = orderDetail.getCourse().getId();
                Course course = courseRepository.findById(courseId).orElseThrow();
                CourseGetVM courseVM = CourseGetVM.fromModel(course);
                totalPrice.updateAndGet(v -> v + orderDetail.getPrice());
                return OrderDetailVM.fromModel(orderDetail, courseVM);
            }).toList();
            Long total = totalPrice.get();
            if (coupon != null) {
                total = total - coupon.getDiscountPercent() * total / 100;
            }
            return OrderVM.fromModel(order, orderDetailVMS, total);
        }).toList();
        return orderVMS;
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatusPostVM orderStatusPostVM) {
        log.info("received orderStatusPostVM: {}", orderStatusPostVM);
        Order order = orderRepository.findById(orderId).orElseThrow();
        if (orderStatusPostVM.status().equals(EOrderStatus.FAILURE) && orderStatusPostVM.reason() != null) {
            order.setReasonFailed(orderStatusPostVM.reason());
        }
        order.setStatus(orderStatusPostVM.status());
        orderRepository.saveAndFlush(order);
    }




    @Override
    public PageableData<OrderVM> getPageableOrders(int pageNum,
                                                   int pageSize,
                                                   Long keyword,
                                                   EOrderStatus status,
                                                   String date
    ) {

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Order> orderPage = null;
        if (keyword != null && status != null) {
            orderPage = orderRepository.findAllCustomWithStatusAndId(pageable, status, keyword, date);
        } else {
            if (keyword != null) {
                orderPage = orderRepository.findAllCustomWithId(pageable, keyword, date);
            } else if (status !=null) {
                orderPage = orderRepository.findAllCustomWithStatus(pageable, status, date);
            } else {
                if (date != null) {
                    orderPage = orderRepository.findAllCustomAndDate(pageable, date);
                } else {
                    orderPage = orderRepository.findAllCustom(pageable);
                }
            }
        }
        List<Order> orders = orderPage.getContent();
        List<OrderVM> orderVMS = orders.stream().map(order -> {
            Long orderId = order.getId();
            Coupon coupon = order.getCoupon();
            AtomicReference<Long> totalPrice = new AtomicReference<>(0L);
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
            List<OrderDetailVM> orderDetailVMS = orderDetails.stream().map(orderDetail -> {
                Long courseId = orderDetail.getCourse().getId();
                Course course = courseRepository.findById(courseId).orElseThrow(() -> new  NotFoundException("course not found"));
                CourseGetVM courseVM = CourseGetVM.fromModel(course);
                totalPrice.updateAndGet(v -> v + orderDetail.getPrice());
                return OrderDetailVM.fromModel(orderDetail, courseVM);
            }).toList();
            Long total = totalPrice.get();
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
