package com.backend.elearning.controller;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.course.CourseGetVM;
import com.backend.elearning.domain.review.*;
import com.backend.elearning.domain.user.UserGetVM;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ReviewController.class,
        excludeAutoConfiguration = {
                UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class
        }
)
public class ReviewControllerTest {

    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createReview_ShouldReturnReview_WhenReviewCreated() throws Exception {
        // Given
        ReviewPostVM reviewPostVM = new ReviewPostVM(
                1L, "Great course!", 5
        );

        UserGetVM userGetVM = new UserGetVM(1L, "John", "Doe", "john.doe@example.com", "photoUrl");
        ReviewVM reviewVM = new ReviewVM(
                1L, "Great course!", 5, userGetVM, "2024-08-29T10:00:00Z", "2024-08-29T10:00:00Z", true
        );

        when(reviewService.createReviewForProduct(reviewPostVM)).thenReturn(reviewVM);

        // When & Then
        mockMvc.perform(post("/api/v1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reviewPostVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Great course!"))
                .andExpect(jsonPath("$.ratingStar").value(5))
                .andExpect(jsonPath("$.student.id").value(1L))
                .andExpect(jsonPath("$.student.firstName").value("John"))
                .andExpect(jsonPath("$.student.lastName").value("Doe"))
                .andExpect(jsonPath("$.student.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.student.photo").value("photoUrl"))
                .andExpect(jsonPath("$.created_at").value("2024-08-29T10:00:00Z"))
                .andExpect(jsonPath("$.updated_at").value("2024-08-29T10:00:00Z"))
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void updateReview_ShouldReturnUpdatedReview_WhenReviewUpdated() throws Exception {
        // Given
        Long reviewId = 1L;
        ReviewPostVM reviewPostVM = new ReviewPostVM(
                1L, "Updated review content", 4
        );

        UserGetVM userGetVM = new UserGetVM(1L, "Jane", "Doe", "jane.doe@example.com", "photoUrl");
        ReviewVM updatedReviewVM = new ReviewVM(
                1L, "Updated review content", 4, userGetVM, "2024-08-30T10:00:00Z", "2024-08-30T10:00:00Z", true
        );

        when(reviewService.updateReview(reviewPostVM, reviewId)).thenReturn(updatedReviewVM);

        // When & Then
        mockMvc.perform(put("/api/v1/reviews/{id}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reviewPostVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("Updated review content"))
                .andExpect(jsonPath("$.ratingStar").value(4))
                .andExpect(jsonPath("$.student.id").value(1L))
                .andExpect(jsonPath("$.student.firstName").value("Jane"))
                .andExpect(jsonPath("$.student.lastName").value("Doe"))
                .andExpect(jsonPath("$.student.email").value("jane.doe@example.com"))
                .andExpect(jsonPath("$.student.photo").value("photoUrl"))
                .andExpect(jsonPath("$.created_at").value("2024-08-30T10:00:00Z"))
                .andExpect(jsonPath("$.updated_at").value("2024-08-30T10:00:00Z"))
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void getPageableReviews_ShouldReturnPagedReviews_WhenValidParams() throws Exception {
        // Given
        int pageNum = 1;
        int pageSize = 10;
        String keyword = "course";

        UserGetVM userGetVM = new UserGetVM(1L, "John", "Doe", "john.doe@example.com", "photoUrl");
        CourseGetVM courseGetVM = new CourseGetVM(
                1L, "Course Title", "Course Headline", "Course Description", "Beginner", "imageUrl"
        );
        ReviewGetListVM reviewGetListVM = new ReviewGetListVM(
                1L, "Great course!", 5, userGetVM, courseGetVM, "2024-08-29T10:00:00Z", "2024-08-29T10:00:00Z", true
        );

        PageableData<ReviewGetListVM> pageableData = new PageableData<>();
        pageableData.setPageNum(pageNum);
        pageableData.setPageSize(pageSize);
        pageableData.setTotalElements(1L);
        pageableData.setTotalPages(1);
        pageableData.setContent(Collections.singletonList(reviewGetListVM));

        when(reviewService.getPageableReviews(pageNum, pageSize, keyword)).thenReturn(pageableData);

        // When & Then
        mockMvc.perform(get("/api/v1/admin/reviews/paging")
                        .param("pageNum", String.valueOf(pageNum))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNum").value(pageNum))
                .andExpect(jsonPath("$.pageSize").value(pageSize))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].content").value("Great course!"))
                .andExpect(jsonPath("$.content[0].ratingStar").value(5))
                .andExpect(jsonPath("$.content[0].student.id").value(1L))
                .andExpect(jsonPath("$.content[0].student.firstName").value("John"))
                .andExpect(jsonPath("$.content[0].student.lastName").value("Doe"))
                .andExpect(jsonPath("$.content[0].student.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.content[0].student.photo").value("photoUrl"))
                .andExpect(jsonPath("$.content[0].course.id").value(1L))
                .andExpect(jsonPath("$.content[0].course.title").value("Course Title"))
                .andExpect(jsonPath("$.content[0].course.headline").value("Course Headline"))
                .andExpect(jsonPath("$.content[0].course.description").value("Course Description"))
                .andExpect(jsonPath("$.content[0].course.level").value("Beginner"))
                .andExpect(jsonPath("$.content[0].course.image").value("imageUrl"))
                .andExpect(jsonPath("$.content[0].createdAt").value("2024-08-29T10:00:00Z"))
                .andExpect(jsonPath("$.content[0].updatedAt").value("2024-08-29T10:00:00Z"))
                .andExpect(jsonPath("$.content[0].status").value(true));
    }

    @Test
    void testUpdateReviewStatus() throws Exception {
        // Given
        Long reviewId = 1L;
        boolean status = true;

        // Mock the behavior of reviewService to do nothing (successful update)
        doNothing().when(reviewService).updateStatusReview(status, reviewId);

        // When
        ResultActions resultActions = mockMvc.perform(put("/api/v1/admin/reviews/{id}/status/{status}", reviewId, status)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isNoContent());
    }
}
