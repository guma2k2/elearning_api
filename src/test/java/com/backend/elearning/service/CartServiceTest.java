package com.backend.elearning.service;

import com.backend.elearning.domain.cart.*;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.course.CourseServiceImpl;
import com.backend.elearning.domain.learning.learningLecture.LearningLectureService;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private  StudentRepository studentRepository;

    @Mock
    private  CourseRepository courseRepository;

    @Mock
    private  CourseServiceImpl courseService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private CartService cartService;


    @BeforeEach
    public void beforeEach() {
        cartService = new CartServiceImpl(cartRepository, studentRepository, courseRepository, courseService);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void addCourseToCart_shouldAddCourseToCart_whenCourseIsNotInCart() {
        // Given
        String email = "student@example.com";
        Long courseId = 1L;
        Student student = new Student();
        Course course = new Course();
        Cart cart = Cart.builder().course(course).student(student).buyLater(false).build();
        CourseListGetVM courseListGetVM = new CourseListGetVM(
                1L,                                      // id
                "Introduction to Java",                  // title
                "Learn the basics of Java programming",  // headline
                "Beginner",                              // level
                "introduction-to-java",                  // slug
                "10h 30m",                               // totalDurationCourse
                25,                                      // totalLectures
                4.7,                                     // averageRating
                1500,                                    // ratingCount
                "https://example.com/images/java.png",   // image
                4999L,
                1999L,
                false,// price
                "John Doe"                               // createdBy
        );

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(cartRepository.findByEmailAndCourseId(courseId, email)).thenReturn(Optional.empty());
        when(cartRepository.saveAndFlush(Mockito.any(Cart.class))).thenReturn(cart);
        when(courseService.getCourseListGetVMById(courseId)).thenReturn(courseListGetVM);

        // When
        CartListGetVM result = cartService.addCourseToCart(courseId);

        // Then
        assertNotNull(result);
        assertEquals(courseListGetVM, result.course());
        assertFalse(result.buyLater());

        verify(cartRepository, times(1)).saveAndFlush(any(Cart.class));
    }

    @Test
    void addCourseToCart_shouldReturnExistedCart_whenCourseIsAlreadyInCart() {
        // Given
        String email = "student@example.com";
        Long courseId = 1L;
        Student student = new Student();
        Course course = new Course();
        Cart existingCart = Cart.builder().course(course).student(student).buyLater(false).build();
        CourseListGetVM courseListGetVM = new CourseListGetVM(
                1L,                                      // id
                "Introduction to Java",                  // title
                "Learn the basics of Java programming",  // headline
                "Beginner",                              // level
                "introduction-to-java",                  // slug
                "10h 30m",                               // totalDurationCourse
                25,                                      // totalLectures
                4.7,                                     // averageRating
                1500,                                    // ratingCount
                "https://example.com/images/java.png",   // image
                4999L,1999L,
                false,// price
                "John Doe"                               // createdBy
        );

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(cartRepository.findByEmailAndCourseId(courseId, email)).thenReturn(Optional.of(existingCart));
        when(courseService.getCourseListGetVMById(courseId)).thenReturn(courseListGetVM);

        // When
        CartListGetVM result = cartService.addCourseToCart(courseId);

        // Then
        assertNotNull(result);
        assertEquals(existingCart.getId(), result.id());
        assertEquals(courseListGetVM, result.course());
        assertFalse(result.buyLater());

        verify(cartRepository, never()).saveAndFlush(any(Cart.class));
    }

    @Test
    void addCourseToCart_shouldThrowNotFoundException_whenStudentNotFound() {
        // Given
        String email = "student@example.com";
        Long courseId = 1L;

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        when(studentRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When and then
        assertThrows(NotFoundException.class, () -> cartService.addCourseToCart(courseId));

        verify(cartRepository, never()).findByEmailAndCourseId(anyLong(), anyString());
        verify(cartRepository, never()).saveAndFlush(any(Cart.class));
    }

    @Test
    void deleteCourseInCart_shouldDeleteCart_whenCartExists() {
        // Given
        Long cartId = 1L;
        Cart cart = new Cart();

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        // When
        cartService.deleteCourseInCart(cartId);

        // Then
        verify(cartRepository, times(1)).delete(cart);
    }

    @Test
    void deleteCourseInCart_shouldThrowException_whenCartDoesNotExist() {
        // Given
        Long cartId = 1L;

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        // When and then
        assertThrows(NotFoundException.class, () -> cartService.deleteCourseInCart(cartId));

        verify(cartRepository, never()).delete(any(Cart.class));
    }

    @Test
    void updateCartBuyLater_shouldUpdateBuyLaterStatus_whenCartExists() {
        // Given
        Long cartId = 1L;
        Cart cart = new Cart();
        cart.setBuyLater(false);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        // When
        cartService.updateCartBuyLater(cartId);

        // Then
        verify(cartRepository, times(1)).updateCartBuyLater(true, cartId);
    }

    @Test
    void updateCartBuyLater_shouldThrowException_whenCartDoesNotExist() {
        // Given
        Long cartId = 1L;

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        // When and then
        assertThrows(NotFoundException.class, () -> cartService.updateCartBuyLater(cartId));

        verify(cartRepository, never()).updateCartBuyLater(anyBoolean(), anyLong());
    }

    @Test
    void listCartForUser_shouldReturnCartListForUser() {
        // Given
        String email = "student@example.com";
        Cart cart1 = new Cart();
        cart1.setId(1L);
        cart1.setBuyLater(false);
        Cart cart2 = new Cart();
        cart2.setId(2L);
        cart2.setBuyLater(true);

        Course course1 = new Course();
        course1.setId(1L);
        cart1.setCourse(course1);

        Course course2 = new Course();
        course2.setId(2L);
        cart2.setCourse(course2);

        CourseListGetVM courseListGetVM1 = new CourseListGetVM(
                1L,                                      // id
                "Introduction to Java",                  // title
                "Learn the basics of Java programming",  // headline
                "Beginner",                              // level
                "introduction-to-java",                  // slug
                "10h 30m",                               // totalDurationCourse
                25,                                      // totalLectures
                4.7,                                     // averageRating
                1500,                                    // ratingCount
                "https://example.com/images/java.png",   // image
                4999L,1999L,
                false,// price
                "John Doe"                               // createdBy
        );
        CourseListGetVM courseListGetVM2 = new CourseListGetVM(
                2L, "Advanced Java", "Deep dive into Java", "Advanced", "advanced-java",
                "15h 45m", 35, 4.9, 3000, "https://example.com/images/java_advanced.png", 7999L, 1999L, false,"Jane Smith"
        );

        List<Cart> carts = List.of(cart1, cart2);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        when(cartRepository.findByUserEmail(email)).thenReturn(carts);
        when(courseService.getCourseListGetVMById(1L)).thenReturn(courseListGetVM1);
        when(courseService.getCourseListGetVMById(2L)).thenReturn(courseListGetVM2);

        // When
        List<CartListGetVM> result = cartService.listCartForUser();

        // Then
        assertEquals(2, result.size());
        assertEquals(courseListGetVM1, result.get(0).course());
        assertEquals(courseListGetVM2, result.get(1).course());
        assertEquals(cart1.isBuyLater(), result.get(0).buyLater());
        assertEquals(cart2.isBuyLater(), result.get(1).buyLater());

        verify(cartRepository, times(1)).findByUserEmail(email);
        verify(courseService, times(1)).getCourseListGetVMById(1L);
        verify(courseService, times(1)).getCourseListGetVMById(2L);
    }
}
