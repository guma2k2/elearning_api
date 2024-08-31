package com.backend.elearning.controller;

import com.backend.elearning.domain.cart.CartController;
import com.backend.elearning.domain.cart.CartListGetVM;
import com.backend.elearning.domain.cart.CartService;
import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.security.JWTUtil;
import com.backend.elearning.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = CartController.class,
        excludeAutoConfiguration = {
                UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class
        }
)
public class CartControllerTest {
    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addToCart_ShouldReturnCartListGetVM_WhenCourseIsAddedToCart() throws Exception {
        // Given
        Long courseId = 1L;
        CourseListGetVM courseListGetVM = new CourseListGetVM(
                1L, "Course Title", "Course Headline", "Beginner",
                "course-slug", "10h 30m", 25, 4.5, 100,
                "image-url", 1999L, false, "Author Name"
        );
        CartListGetVM cartListGetVM = new CartListGetVM(1L, courseListGetVM, false);
        when(cartService.addCourseToCart(courseId)).thenReturn(cartListGetVM);
        // When & Then
        mockMvc.perform(post("/api/v1/carts/add-to-cart/course/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(cartListGetVM.id()))
                .andExpect(jsonPath("$.course.id").value(cartListGetVM.course().id()))
                .andExpect(jsonPath("$.buyLater").value(cartListGetVM.buyLater()));
    }

    @Test
    void listCartForCustomer_ShouldReturnListOfCartListGetVM() throws Exception {
        // Given
        CourseListGetVM course1 = new CourseListGetVM(
                1L, "Course Title 1", "Course Headline 1", "Beginner",
                "course-slug-1", "10h 30m", 25, 4.5, 100,
                "image-url-1", 1999L, false, "Author Name 1"
        );
        CourseListGetVM course2 = new CourseListGetVM(
                2L, "Course Title 2", "Course Headline 2", "Intermediate",
                "course-slug-2", "8h 15m", 20, 4.2, 80,
                "image-url-2", 2999L, false, "Author Name 2"
        );
        List<CartListGetVM> cartList = List.of(
                new CartListGetVM(1L, course1, false),
                new CartListGetVM(2L, course2, true)
        );
        when(cartService.listCartForUser()).thenReturn(cartList);

        // When & Then
        mockMvc.perform(get("/api/v1/carts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(cartList.size()))
                .andExpect(jsonPath("$[0].id").value(cartList.get(0).id()))
                .andExpect(jsonPath("$[0].course.id").value(cartList.get(0).course().id()))
                .andExpect(jsonPath("$[0].buyLater").value(cartList.get(0).buyLater()))
                .andExpect(jsonPath("$[1].id").value(cartList.get(1).id()))
                .andExpect(jsonPath("$[1].course.id").value(cartList.get(1).course().id()))
                .andExpect(jsonPath("$[1].buyLater").value(cartList.get(1).buyLater()));
    }

    @Test
    void deleteCart_ShouldReturnNoContent_WhenCartIsDeleted() throws Exception {
        // Given
        Long cartId = 1L;
        doNothing().when(cartService).deleteCourseInCart(cartId);

        // When & Then
        mockMvc.perform(delete("/api/v1/carts/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateCartStatus_ShouldReturnNoContent_WhenCartStatusIsUpdated() throws Exception {
        // Given
        Long cartId = 1L;
        doNothing().when(cartService).updateCartBuyLater(cartId);

        // When & Then
        mockMvc.perform(put("/api/v1/carts/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
