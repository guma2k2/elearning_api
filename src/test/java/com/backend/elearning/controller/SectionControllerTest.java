package com.backend.elearning.controller;

import com.backend.elearning.domain.quiz.QuizController;
import com.backend.elearning.domain.section.SectionController;
import com.backend.elearning.domain.section.SectionPostVM;
import com.backend.elearning.domain.section.SectionService;
import com.backend.elearning.domain.section.SectionVM;
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

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = SectionController.class,
        excludeAutoConfiguration = {
                UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class
        }
)
public class SectionControllerTest {

    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private SectionService sectionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCourse_ShouldReturnCreatedSection_WhenValidInput() throws Exception {
        // Given
        SectionPostVM sectionPostVM = new SectionPostVM(null, "Section 1", 1.0f, "Objective 1", 1L);
        SectionVM sectionVM = new SectionVM(1L, "Section 1", 1.0f, "Objective 1", new ArrayList<>());

        when(sectionService.create(any(SectionPostVM.class))).thenReturn(sectionVM);

        // When & Then
        mockMvc.perform(post("/api/v1/admin/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sectionPostVM)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sectionVM.id()))
                .andExpect(jsonPath("$.title").value(sectionVM.title()))
                .andExpect(jsonPath("$.number").value(sectionVM.number()))
                .andExpect(jsonPath("$.objective").value(sectionVM.objective()));
    }

    @Test
    void getSectionById_ShouldReturnSection_WhenFound() throws Exception {
        // Given
        Long sectionId = 1L;
        SectionVM sectionVM = new SectionVM(sectionId, "Section 1", 1.0f, "Objective 1", new ArrayList<>());

        when(sectionService.getById(sectionId, null)).thenReturn(sectionVM);

        // When & Then
        mockMvc.perform(get("/api/v1/admin/sections/{id}", sectionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sectionVM.id()))
                .andExpect(jsonPath("$.title").value(sectionVM.title()))
                .andExpect(jsonPath("$.number").value(sectionVM.number()))
                .andExpect(jsonPath("$.objective").value(sectionVM.objective()));
    }

    @Test
    void updateCourse_ShouldReturnUpdatedSection_WhenValidInput() throws Exception {
        // Given
        Long sectionId = 1L;
        SectionPostVM sectionPostVM = new SectionPostVM(sectionId, "Updated Section", 2.0f, "Updated Objective", 1L);
        SectionVM sectionVM = new SectionVM(sectionId, "Updated Section", 2.0f, "Updated Objective", new ArrayList<>());

        when(sectionService.update(any(SectionPostVM.class), eq(sectionId))).thenReturn(sectionVM);

        // When & Then
        mockMvc.perform(put("/api/v1/admin/sections/{id}", sectionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sectionPostVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sectionVM.id()))
                .andExpect(jsonPath("$.title").value(sectionVM.title()))
                .andExpect(jsonPath("$.number").value(sectionVM.number()))
                .andExpect(jsonPath("$.objective").value(sectionVM.objective()));
    }

    @Test
    void deleteSectionById_ShouldReturnNoContent_WhenSectionDeleted() throws Exception {
        // Given
        Long sectionId = 1L;

        doNothing().when(sectionService).delete(sectionId);

        // When & Then
        mockMvc.perform(delete("/api/v1/admin/sections/{id}", sectionId))
                .andExpect(status().isNoContent());
    }
}
