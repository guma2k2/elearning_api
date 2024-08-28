package com.backend.elearning.service;

import com.backend.elearning.domain.cart.Cart;
import com.backend.elearning.domain.cart.CartRepository;
import com.backend.elearning.domain.coupon.Coupon;
import com.backend.elearning.domain.coupon.CouponRepository;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.order.*;
import com.backend.elearning.domain.order.impl.OrderServiceImpl;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CartRepository cartRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, orderDetailRepository, studentRepository, courseRepository, couponRepository, cartRepository);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("student@example.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testCreateOrder_WithCoupon() {
        // Given
        String couponCode = "DISCOUNT10";
        Coupon coupon = new Coupon();
        coupon.setId(1L);
        OrderPostDto orderPostDto = new OrderPostDto(couponCode, List.of(new OrderDetailPostDto(1L, 100l)));

        Student student = new Student();
        student.setEmail("student@example.com");

        Order order = new Order();
        order.setId(1L);
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(1L);

        when(couponRepository.findByCode(couponCode)).thenReturn(Optional.of(coupon));
        when(studentRepository.findByEmail("student@example.com")).thenReturn(Optional.of(student));
        when(orderRepository.saveAndFlush(Mockito.any(Order.class))).thenReturn(order);
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(new Course()));
        when(cartRepository.findByUserEmailWithBuyLater("student@example.com")).thenReturn(new ArrayList<>());

        // When
        Long orderId = orderService.createOrder(orderPostDto);

        // Then
        assertNotNull(orderId);
        verify(orderRepository, times(1)).saveAndFlush(any(Order.class));
        verify(orderDetailRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testCreateOrder_WithoutCoupon() {
        // Given
        OrderPostDto orderPostDto = new OrderPostDto(null, List.of(new OrderDetailPostDto(1L, 100L)));

        Student student = new Student();
        student.setEmail("student@example.com");

        Order order = new Order();
        order.setId(1L);

        when(studentRepository.findByEmail("student@example.com")).thenReturn(Optional.of(student));
        when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(order);
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(new Course()));
        when(cartRepository.findByUserEmailWithBuyLater("student@example.com")).thenReturn(new ArrayList<>());

        // When
        Long orderId = orderService.createOrder(orderPostDto);

        // Then
        assertNotNull(orderId);
        verify(orderRepository, times(1)).saveAndFlush(any(Order.class));
        verify(orderDetailRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testCreateOrder_WithInvalidStudent() {
        // Given
        OrderPostDto orderPostDto = new OrderPostDto(null, List.of(new OrderDetailPostDto(1L, 100l)));

        when(studentRepository.findByEmail("student@example.com")).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> orderService.createOrder(orderPostDto));
    }

    @Test
    void testCreateOrder_WithInvalidCourse() {
        // Given
        OrderPostDto orderPostDto = new OrderPostDto(null, List.of(new OrderDetailPostDto(1L, 100l)));

        Student student = new Student();
        student.setEmail("student@example.com");

        when(studentRepository.findByEmail("student@example.com")).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> orderService.createOrder(orderPostDto));
    }

    @Test
    void testCreateOrder_WithCartClearance() {
        // Given
        OrderPostDto orderPostDto = new OrderPostDto(null, List.of(new OrderDetailPostDto(1L, 100l)));

        Student student = new Student();
        student.setEmail("student@example.com");

        Order order = new Order();
        order.setId(1L);

        Cart cart = new Cart();
        when(studentRepository.findByEmail("student@example.com")).thenReturn(Optional.of(student));
        when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(order);
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(new Course()));
        when(cartRepository.findByUserEmailWithBuyLater("student@example.com")).thenReturn(List.of(cart));

        // When
        Long orderId = orderService.createOrder(orderPostDto);

        // Then
        verify(cartRepository, times(1)).delete(cart);
    }
}
