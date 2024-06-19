package com.backend.elearning.domain.cart;

import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.course.CourseRepository;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public void addCourseToCart(Long courseId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        if (cartRepository.findByEmailAndCourseId(courseId, email).isEmpty()) {
            Cart cart = Cart.builder()
                    .course(course)
                    .user(user)
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
    public List<CartListGetVM> listCartForUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Cart> carts = cartRepository.findByUserEmail(email);
        List<CartListGetVM> cartListGetVMS = carts.stream().map(cart -> {
            User user = cart.getUser();
            Course course = cart.getCourse();
            String fullName = user.getFirstName().concat(" ").concat(user.getLastName());
            CourseListGetVM courseListGetVM = CourseListGetVM.fromModel(course, 1, 1, 5, 5);
            return new CartListGetVM(fullName, courseListGetVM);
        }).toList();
        return cartListGetVMS;
    }
}
