package com.backend.elearning.controller;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.course.CourseGetVM;
import com.backend.elearning.domain.order.*;
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

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = OrderController.class,
        excludeAutoConfiguration = {
                UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class
        }
)
public class OrderControllerTest {

    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private  OrderDetailService orderDetailService;

    @Test
    void createOrder_ShouldReturnOrderId_WhenOrderCreated() throws Exception {
        // Given
        OrderPostDto orderPostDto = new OrderPostDto(
                "COUPON123",
                Arrays.asList(new OrderDetailPostDto(1L, 100L), new OrderDetailPostDto(2L, 200L))
        );
        Long orderId = 1L;

        when(orderService.createOrder(orderPostDto)).thenReturn(orderId);

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderPostDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(orderId));
    }

    @Test
    void updateOrderStatus_ShouldReturnNoContent_WhenStatusUpdated() throws Exception {
        // Given
        Long orderId = 1L;
        String status = "Shipped";

        doNothing().when(orderService).updateOrderStatus(orderId, status);

        // When & Then
        mockMvc.perform(put("/api/v1/orders/{orderId}/status/{orderStatus}", orderId, status))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPageableOrders_ShouldReturnPagedOrders_WhenValidParams() throws Exception {
        // Given
        int pageNum = 1;
        int pageSize = 10;
        Long orderId = 1L;

        OrderVM orderVM = new OrderVM(
                1L, "studentName", "COUPON123", "2024-08-29T10:00:00Z", "Processing",
                Arrays.asList(new OrderDetailVM(
                        1L, new CourseGetVM(1L, "Course Title", "Course Headline", "Course Description", "Beginner", "imageUrl"), 100L
                )), 100L
        );

        PageableData<OrderVM> pageableData = new PageableData<>();
        pageableData.setPageNum(pageNum);
        pageableData.setPageSize(pageSize);
        pageableData.setTotalElements(1L);
        pageableData.setTotalPages(1);
        pageableData.setContent(Collections.singletonList(orderVM));

        when(orderService.getPageableOrders(pageNum, pageSize, orderId)).thenReturn(pageableData);

        // When & Then
        mockMvc.perform(get("/api/v1/admin/orders/paging")
                        .param("pageNum", String.valueOf(pageNum))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("orderId", String.valueOf(orderId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNum").value(pageNum))
                .andExpect(jsonPath("$.pageSize").value(pageSize))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].student").value("studentName"))
                .andExpect(jsonPath("$.content[0].coupon").value("COUPON123"))
                .andExpect(jsonPath("$.content[0].createdAt").value("2024-08-29T10:00:00Z"))
                .andExpect(jsonPath("$.content[0].status").value("Processing"))
                .andExpect(jsonPath("$.content[0].totalPrice").value(100L))
                .andExpect(jsonPath("$.content[0].orderDetails[0].id").value(1))
                .andExpect(jsonPath("$.content[0].orderDetails[0].course.id").value(1))
                .andExpect(jsonPath("$.content[0].orderDetails[0].course.title").value("Course Title"))
                .andExpect(jsonPath("$.content[0].orderDetails[0].course.headline").value("Course Headline"))
                .andExpect(jsonPath("$.content[0].orderDetails[0].course.description").value("Course Description"))
                .andExpect(jsonPath("$.content[0].orderDetails[0].course.level").value("Beginner"))
                .andExpect(jsonPath("$.content[0].orderDetails[0].course.image").value("imageUrl"))
                .andExpect(jsonPath("$.content[0].orderDetails[0].price").value(100L));
    }

}
