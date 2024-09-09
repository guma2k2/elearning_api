package com.backend.elearning.repository;

import com.backend.elearning.TestConfig;
import com.backend.elearning.domain.cart.Cart;
import com.backend.elearning.domain.coupon.Coupon;
import com.backend.elearning.domain.coupon.CouponRepository;
import com.backend.elearning.domain.order.EOrderStatus;
import com.backend.elearning.domain.order.Order;
import com.backend.elearning.domain.order.OrderRepository;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TestConfig.class})
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        // Clearing the repository before each test
        orderRepository.deleteAll();
        studentRepository.deleteAll();
        couponRepository.deleteAll();

        // Setting up test data
        Student student = Student.builder()
                .email("student@example.com")
                .firstName("firstName")
                .lastName("lastName")
                .password("1234567")
                .build();

        Coupon coupon = Coupon.builder()
                .code("SAVE10")
                .discountPercent(10)
                .startTime(LocalDateTime.now().minusDays(1))
                .endTime(LocalDateTime.now().plusDays(1))
                .build();

        studentRepository.save(student);
        couponRepository.save(coupon);

        Order order1 = Order.builder()
                .status(EOrderStatus.PENDING)
                .student(student)
                .coupon(coupon)
                .build();

        Order order2 = Order.builder()
                .status(EOrderStatus.PENDING)
                .student(student)
                .coupon(coupon)
                .build();

        orderRepository.saveAll(List.of(order1, order2));
    }

    @Test
    @Transactional
    @DirtiesContext
    void testUpdateOrderStatus() {
        // Given
        Long orderId = orderRepository.findAll().get(0).getId();
        EOrderStatus newStatus = EOrderStatus.SUCCESS;

        // When
        orderRepository.updateOrderStatus(orderId, newStatus);

        // Then
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        assertThat(orderOptional)
                .isPresent()
                .hasValueSatisfying(c -> {
                    entityManager.refresh(c);
                    assertThat(c.getStatus()).isEqualTo(newStatus);
                });
    }

    @Test
    void testFindAllByStudent_WithExistingEmail() {
        // Given
        String email = "student@example.com";

        // When
        List<Order> orders = orderRepository.findAllByStudent(email);

        // Then
        assertFalse(orders.isEmpty(), "Orders should be found for the given student email");
        assertTrue(orders.stream().allMatch(o -> o.getStudent().getEmail().equals(email)),
                "All orders should belong to the student with the given email");
    }

    @Test
    void testFindAllByStudent_WithNonExistingEmail() {
        // Given
        String email = "nonexistent@example.com";

        // When
        List<Order> orders = orderRepository.findAllByStudent(email);

        // Then
        assertTrue(orders.isEmpty(), "No orders should be found for a non-existing student email");
    }
}
