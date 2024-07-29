package com.backend.elearning.controller;

import com.backend.elearning.domain.learning.learningLecture.LearningLectureController;
import com.backend.elearning.domain.learning.learningLecture.LearningLecturePostVM;
import com.backend.elearning.domain.learning.learningLecture.LearningLectureService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = LearningLectureController.class,
        excludeAutoConfiguration = {
                UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class
        }
)
public class LearningLectureControllerTest {

    @MockBean
    private LearningLectureService learningLectureService;

    @MockBean
    private JWTUtil jwtUtil;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void testCreateLearningLecture() throws Exception {
        // Given
        LearningLecturePostVM request = new LearningLecturePostVM(1L, 120, true);

        // Mocking the service call
        doNothing().when(learningLectureService).create(request);

        // When
        String requestBody = objectMapper.writeValueAsString(request);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/learning-lectures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

}
