package com.backend.elearning.controller;

import com.backend.elearning.domain.coupon.CouponController;
import com.backend.elearning.domain.course.CourseController;
import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.course.CourseService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = CourseController.class,
        excludeAutoConfiguration = {
                UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class
        }
)
public class CourseControllerTest {

    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    @Test
    void testGetPageableCourseByCategoryId() throws Exception {
        // Given
        Integer categoryId = 1;

        List<CourseListGetVM> mockCourseList = Arrays.asList(
                new CourseListGetVM(1L, "Course 1", "Headline 1", "Beginner", "course-1",
                        "10 hours", 12, 4.5, 100, "image1.jpg", 1000L, true, "John Doe"),
                new CourseListGetVM(2L, "Course 2", "Headline 2", "Intermediate", "course-2",
                        "15 hours", 20, 4.7, 200, "image2.jpg", 1500L, false, "Jane Doe")
        );

        // Mock the behavior of courseService
        when(courseService.getCoursesByCategoryId(categoryId)).thenReturn(mockCourseList);

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/courses/category/{categoryId}", categoryId)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(mockCourseList)));
    }

    @Test
    void testDeleteCourseById() throws Exception {
        // Given
        Long courseId = 1L;

        // Mock the behavior of courseService to do nothing when deleting the course
        doNothing().when(courseService).delete(courseId);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/admin/courses/{id}", courseId)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isNoContent());

        // Verify that the delete method in courseService was called with the correct courseId
        verify(courseService).delete(courseId);
    }
}
