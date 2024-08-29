package com.backend.elearning.controller;

import com.backend.elearning.domain.common.ECurriculumType;
import com.backend.elearning.domain.lecture.*;
import com.backend.elearning.domain.section.Section;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = LectureController.class,
        excludeAutoConfiguration = {
                UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class
        }
)
public class LectureControllerTest {
    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LectureService lectureService;

    @Test
    void createCourse_ShouldReturnCreated_WhenLectureIsCreated() throws Exception {
        // Given
        Section section = new Section(); // Mock or create a Section instance
        section.setId(1L); // Set section ID
        LecturePostVM lecturePostVM = new LecturePostVM(1L, "Lecture Title", 1.0f, "Lecture Details", 60, "video.mp4", section.getId());
        Lecture lecture = new Lecture(lecturePostVM, section);
        LectureVm lectureVm = new LectureVm(lecture);

        when(lectureService.create(any(LecturePostVM.class))).thenReturn(lectureVm);


        // When & Then
        mockMvc.perform(post("/api/v1/admin/lectures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lecturePostVM)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(lectureVm.getTitle()))
                .andExpect(jsonPath("$.videoId").value(lectureVm.getVideoId()));
    }

    @Test
    void update_ShouldReturnOk_WhenLectureIsUpdated() throws Exception {
        // Given
        Long lectureId = 1L;
        Section section = new Section();
        section.setId(2L); // Set a different section ID
        LecturePostVM lecturePutVM = new LecturePostVM(lectureId, "Updated Lecture Title", 2.0f, "Updated Lecture Details", 90, "updated_video.mp4", section.getId());
        Lecture lecture = new Lecture(lecturePutVM, section);
        LectureVm lectureVm = new LectureVm(lecture);

        when(lectureService.update(any(LecturePostVM.class), eq(lectureId))).thenReturn(lectureVm);

        // When & Then
        mockMvc.perform(put("/api/v1/admin/lectures/{id}", lectureId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lecturePutVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(lectureVm.getTitle()))
                .andExpect(jsonPath("$.videoId").value(lectureVm.getVideoId()));
    }

    @Test
    void deleteLectureById_ShouldReturnNoContent_WhenLectureIsDeleted() throws Exception {
        // Given
        Long lectureId = 1L;
        doNothing().when(lectureService).delete(lectureId);

        // When & Then
        mockMvc.perform(delete("/api/v1/admin/lectures/{id}", lectureId))
                .andExpect(status().isNoContent());
    }

}
