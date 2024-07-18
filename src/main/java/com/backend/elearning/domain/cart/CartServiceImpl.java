package com.backend.elearning.domain.cart;

import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.student.StudentRepository;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserRepository;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public CartServiceImpl(CartRepository cartRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.cartRepository = cartRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public void addCourseToCart(Long courseId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.STUDENT_NOT_FOUND));
        Course course = courseRepository.findById(courseId).orElseThrow();

        if (cartRepository.findByEmailAndCourseId(courseId, email).isEmpty()) {
            Cart cart = Cart.builder()
                    .course(course)
                    .student(student)
                    .buyLater(false)
                    .build();

            cartRepository.saveAndFlush(cart);
        }

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
            CourseListGetVM courseListGetVM = CourseListGetVM.fromModel(course, 1+"", 1, 5, 5);
            return new CartListGetVM(cart.getId(), courseListGetVM, cart.isBuyLater());
        }).toList();
        return cartListGetVMS;
    }
}
