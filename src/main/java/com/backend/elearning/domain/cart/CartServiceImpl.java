package com.backend.elearning.domain.cart;

import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.course.CourseServiceImpl;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    private final CourseServiceImpl courseService;

    public CartServiceImpl(CartRepository cartRepository, StudentRepository studentRepository, CourseRepository courseRepository, CourseServiceImpl courseService) {
        this.cartRepository = cartRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.courseService = courseService;
    }

    @Override
    public CartListGetVM addCourseToCart(Long courseId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.STUDENT_NOT_FOUND));
        Course course = courseRepository.findById(courseId).orElseThrow();
        Optional<Cart> cartOptional = cartRepository.findByEmailAndCourseId(courseId, email);
        if (cartOptional.isEmpty()) {
            Cart cart = Cart.builder()
                    .course(course)
                    .student(student)
                    .buyLater(false)
                    .build();

            cartRepository.saveAndFlush(cart);
            CourseListGetVM courseListGetVM = courseService.getCourseListGetVMById(courseId);
            return new CartListGetVM(cart.getId(), courseListGetVM, cart.isBuyLater());
        }
        Cart existedCart = cartOptional.get();
        CourseListGetVM courseListGetVM = courseService.getCourseListGetVMById(courseId);
        return new CartListGetVM(existedCart.getId(), courseListGetVM, existedCart.isBuyLater());
    }

    @Override
    @Transactional
    public void deleteCourseInCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow();
        cartRepository.delete(cart);
    }

    @Override
    @Transactional
    public void updateCartBuyLater(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow();
        boolean newStatus = !cart.isBuyLater();
        cartRepository.updateCartBuyLater(newStatus, cartId);
    }

    @Override
    public List<CartListGetVM> listCartForUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Cart> carts = cartRepository.findByUserEmail(email);
        List<CartListGetVM> cartListGetVMS = carts.stream().map(cart -> {
            Course course = cart.getCourse();
            CourseListGetVM courseListGetVM = courseService.getCourseListGetVMById(course.getId());
            return new CartListGetVM(cart.getId(), courseListGetVM, cart.isBuyLater());
        }).toList();
        return cartListGetVMS;
    }
}
