package com.backend.elearning.repository;

import com.backend.elearning.TestConfig;
import com.backend.elearning.domain.cart.Cart;
import com.backend.elearning.domain.cart.CartRepository;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestConfig.class})
public class CartRepositoryTest {

    @Autowired
    private CartRepository underTest;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository ;
    Faker faker = new Faker();

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    @DirtiesContext
        // Reload context to ensure a clean state
    void existCartByEmailAndCourse() {
        // given
        String email = "thuan@gmail.com";
        Long courseId = 1L;
        Student student = Student.builder().email(email)
                .firstName("firstName")
                .lastName("lastName")
                .password("1234567")
                .build();
        Course course = Course.builder().id(courseId).title("Course title").build();

        studentRepository.saveAndFlush(student);
        courseRepository.saveAndFlush(course);
        Cart cart = Cart.builder().buyLater(false).course(course).student(student).build();

        underTest.saveAndFlush(cart);

        // when
        Optional<Cart> cartOptional = underTest.findByEmailAndCourseId(courseId, email);

        // then
        assertThat(cartOptional).isNotEmpty();
        assertThat(cartOptional)
                .isPresent()
                .hasValueSatisfying(
                        c -> assertThat(c.isBuyLater()).isEqualTo(false)
                );
    }

    @Test
    @Transactional
    @DirtiesContext // Reload context to ensure a clean state
    void existCartFailByEmailAndCourse() {
        // given
        String FakeEmail = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String email = "thuan@gmail.com";
        Long courseId = 1L;
        Student student = Student.builder().email(email).firstName("firstName")
                .lastName("lastName")
                .password("1234567").build();
        Course course = Course.builder().id(courseId).title("Course title").build();

        studentRepository.saveAndFlush(student);
        courseRepository.saveAndFlush(course);
        Cart cart = Cart.builder().buyLater(false).course(course).student(student).build();
        underTest.saveAndFlush(cart);

        // when
        Optional<Cart> cartOptional = underTest.findByEmailAndCourseId(courseId, FakeEmail);

        // then
        assertThat(cartOptional).isNotPresent();
    }

    @Test
    @Transactional
    @DirtiesContext // Reload context to ensure a clean state
    void canUpdateBuyLaterByCartId() {
        // given
        String email = "thuan@gmail.com";
        Long courseId = 1L;
        boolean expected = true;
        Student student = Student.builder().email(email).firstName("firstName")
                .lastName("lastName")
                .password("1234567").build();
        Course course = Course.builder().id(courseId).title("Course title").build();

        Cart cart = Cart.builder().buyLater(false).course(course).student(student).build();
        studentRepository.saveAndFlush(student);
        courseRepository.saveAndFlush(course);
        underTest.saveAndFlush(cart);

        // when
        underTest.updateCartBuyLater(expected, cart.getId());
        underTest.flush();

        // then
        Optional<Cart> cartOptional = underTest.findById(cart.getId());
        assertThat(cartOptional)
                .isPresent()
                .hasValueSatisfying(c -> {
                    entityManager.refresh(c);
                    assertThat(c.isBuyLater()).isEqualTo(expected);
                });
    }
}
