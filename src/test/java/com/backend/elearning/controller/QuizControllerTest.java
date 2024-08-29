package com.backend.elearning.controller;

import com.backend.elearning.domain.quiz.*;
import com.backend.elearning.domain.section.Section;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.security.JWTUtil;
import com.backend.elearning.security.UserDetailsServiceImpl;
import com.backend.elearning.utils.Constants;
import com.backend.elearning.utils.MessageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = QuizController.class,
        excludeAutoConfiguration = {
                UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class
        }
)
public class QuizControllerTest {

    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizService quizService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCourse_ShouldReturnCreated_WhenQuizIsCreated() throws Exception {
        // Given
        Section section = new Section(); // Mock or create a Section instance
        section.setId(1L); // Set section ID
        QuizPostVM quizPostVM = new QuizPostVM(1L, "Quiz Title", 1.0f, "Quiz Description", section.getId());
        Quiz quiz = new Quiz(quizPostVM, section); // Assuming a constructor exists in Quiz class that accepts QuizPostVM and Section
        QuizVM quizVM = new QuizVM(quiz);

        when(quizService.create(any(QuizPostVM.class))).thenReturn(quizVM);

        // When & Then
        mockMvc.perform(post("/api/v1/admin/quizzes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quizPostVM)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(quizVM.getTitle()))
                .andExpect(jsonPath("$.description").value(quizVM.getDescription()));
    }

    @Test
    void update_ShouldReturnOk_WhenQuizIsUpdated() throws Exception {
        // Given
        Long quizId = 1L;
        Section section = new Section(); // Mock or create a Section instance
        section.setId(2L); // Set a different section ID
        QuizPostVM quizPostVM = new QuizPostVM(quizId, "Updated Quiz Title", 2.0f, "Updated Quiz Description", section.getId());
        Quiz quiz = new Quiz(quizPostVM, section); // Assuming a constructor exists in Quiz class that accepts QuizPostVM and Section
        QuizVM quizVM = new QuizVM(quiz);

        when(quizService.update(any(QuizPostVM.class), eq(quizId))).thenReturn(quizVM);

        // When & Then
        mockMvc.perform(put("/api/v1/admin/quizzes/{id}", quizId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quizPostVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(quizVM.getTitle()))
                .andExpect(jsonPath("$.description").value(quizVM.getDescription()));
    }

    @Test
    void deleteQuizById_ShouldReturnNoContent_WhenQuizIsDeleted() throws Exception {
        // Given
        Long quizId = 1L;
        doNothing().when(quizService).delete(quizId);

        // When & Then
        mockMvc.perform(delete("/api/v1/admin/quizzes/{id}", quizId))
                .andExpect(status().isNoContent());
    }



    @Test
    void deleteQuizById_ShouldReturnNotFound_WhenQuizNotFound() throws Exception {
        // Given
        Long quizId = 1L;

        // Use doThrow for methods that return void
        doThrow(new NotFoundException(MessageUtil.getMessage(Constants.ERROR_CODE.QUIZ_NOT_FOUND, quizId))).when(quizService).delete(quizId);

        // When & Then
        mockMvc.perform(delete("/api/v1/admin/quizzes/{id}", quizId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.details").value(MessageUtil.getMessage(Constants.ERROR_CODE.QUIZ_NOT_FOUND, quizId)));
    }
}
