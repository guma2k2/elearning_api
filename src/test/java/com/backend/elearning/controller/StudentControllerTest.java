package com.backend.elearning.controller;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.quiz.QuizController;
import com.backend.elearning.domain.student.StudentController;
import com.backend.elearning.domain.student.StudentListGetVM;
import com.backend.elearning.domain.student.StudentPutVM;
import com.backend.elearning.domain.student.StudentService;
import com.backend.elearning.domain.topic.TopicService;
import com.backend.elearning.domain.user.UserVm;
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

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = StudentController.class,
        excludeAutoConfiguration = {
                UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class
        }
)
public class StudentControllerTest {
    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getStudents_ShouldReturnPagedStudents_WhenValidParams() throws Exception {
        // Given
        int pageNum = 1;
        int pageSize = 10;
        String keyword = "test";

        PageableData<StudentListGetVM> pageableData = new PageableData<>();
        pageableData.setPageNum(pageNum);
        pageableData.setPageSize(pageSize);
        pageableData.setTotalElements(1L);
        pageableData.setTotalPages(1);
        pageableData.setContent(Collections.singletonList(new StudentListGetVM(
                1L, "test@example.com", "John", "Doe", "Male", true, "photoUrl", "1990-01-01"
        )));

        when(studentService.getStudents(pageNum, pageSize, keyword)).thenReturn(pageableData);

        // When & Then
        mockMvc.perform(get("/api/v1/admin/students/paging")
                        .param("pageNum", String.valueOf(pageNum))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNum").value(pageNum))
                .andExpect(jsonPath("$.pageSize").value(pageSize))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].email").value("test@example.com"))
                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                .andExpect(jsonPath("$.content[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.content[0].gender").value("Male"))
                .andExpect(jsonPath("$.content[0].active").value(true))
                .andExpect(jsonPath("$.content[0].photo").value("photoUrl"))
                .andExpect(jsonPath("$.content[0].dateOfBirth").value("1990-01-01"));
    }

    @Test
    void updateStatus_ShouldReturnNoContent_WhenStatusUpdated() throws Exception {
        // Given
        Long studentId = 1L;
        boolean status = true;

        doNothing().when(studentService).updateStatusStudent(status, studentId);

        // When & Then
        mockMvc.perform(put("/api/v1/admin/students/{id}/status/{status}", studentId, status))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateProfile_ShouldReturnUpdatedUserVm_WhenProfileUpdated() throws Exception {
        // Given
        StudentPutVM studentPutVM = new StudentPutVM(
                1L, "test@example.com", "John", "Doe", "password", "Male", "photoUrl", 1, 1, 1990
        );
        UserVm updatedUserVm = new UserVm(
                1L, "test@example.com", "John", "Doe", "Male", true, "photoUrl", "1990-01-01", "student"
        );

        when(studentService.updateProfileStudent(studentPutVM)).thenReturn(updatedUserVm);

        // When & Then
        mockMvc.perform(put("/api/v1/students/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(studentPutVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.photoURL").value("photoUrl"))
                .andExpect(jsonPath("$.dateOfBirth").value("1990-01-01"))
                .andExpect(jsonPath("$.role").value("student"));
    }
}
