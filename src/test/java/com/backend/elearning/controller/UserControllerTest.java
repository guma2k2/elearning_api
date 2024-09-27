package com.backend.elearning.controller;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.course.CourseService;
import com.backend.elearning.domain.user.*;
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

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class,
        excludeAutoConfiguration = {
                UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class
        }
)
public class UserControllerTest {

    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    @Test
    void getUser_ShouldReturnPagedUsers_WhenValidParams() throws Exception {
        // Given
        int pageNum = 1;
        int pageSize = 10;
        String keyword = "test";
        PageableData<UserVm> pageableData = new PageableData<>();
        pageableData.setPageNum(pageNum);
        pageableData.setPageSize(pageSize);
        pageableData.setTotalElements(1L);
        pageableData.setTotalPages(1);
        pageableData.setContent(Collections.singletonList(new UserVm(1L, "test@example.com", "John", "Doe", "Male", true, "photoURL", "01/01/1990", "USER")));

        when(userService.getUsers(pageNum, pageSize, keyword)).thenReturn(pageableData);

        // When & Then
        mockMvc.perform(get("/api/v1/admin/users/paging")
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
                .andExpect(jsonPath("$.content[0].photoURL").value("photoURL"))
                .andExpect(jsonPath("$.content[0].dateOfBirth").value("01/01/1990"))
                .andExpect(jsonPath("$.content[0].role").value("USER"));
    }

    @Test
    void create_ShouldReturnCreatedUser_WhenValidInput() throws Exception {
        // Given
        UserPostVm userPostVm = new UserPostVm(
                "new@example.com",
                "Jane",
                "Doe",
                "password123",
                "Female",
                true,
                "photoURL",
                2,
                2,
                1992,
                "USER"
        );
        UserVm createdUser = new UserVm(1L, "new@example.com", "Jane", "Doe", "Female", true, "photoURL", "02/02/1992", "USER");

        when(userService.create(userPostVm)).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPostVm)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.gender").value("Female"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.photoURL").value("photoURL"))
                .andExpect(jsonPath("$.dateOfBirth").value("02/02/1992"))
                .andExpect(jsonPath("$.role").value("USER"));
    }


    @Test
    void update_ShouldReturnUpdatedUser_WhenValidInput() throws Exception {
        // Given
        Long userId = 1L;
        UserPutVm userPutVm = new UserPutVm(
                userId,
                "updated@example.com",
                "Janet",
                "Doe",
                "newpassword123",
                "Female",
                true,
                "newPhotoURL",
                3,
                3,
                1993,
                "USER"
        );
        UserVm updatedUser = new UserVm(userId, "updated@example.com", "Janet", "Doe", "Female", true, "newPhotoURL", "03/03/1993", "USER");

        when(userService.update(userPutVm, userId)).thenReturn(updatedUser);

        // When & Then
        mockMvc.perform(put("/api/v1/admin/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPutVm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.firstName").value("Janet"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.gender").value("Female"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.photoURL").value("newPhotoURL"))
                .andExpect(jsonPath("$.dateOfBirth").value("03/03/1993"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void delete_ShouldReturnNoContent_WhenUserDeleted() throws Exception {
        // Given
        Long userId = 1L;
        doNothing().when(userService).delete(userId);

        // When & Then
        mockMvc.perform(delete("/api/v1/admin/users/{id}", userId))
                .andExpect(status().isNoContent());
    }
    @Test
    void testGetUserProfile_WithEmptyCourses() throws Exception {
        // Given
        Long userId = 1L;

        // Create UserGetDetailVm object with empty courses
        UserGetDetailVm userGetDetailVm = new UserGetDetailVm();
        userGetDetailVm.setId(userId);
        userGetDetailVm.setEmail("test@example.com");
        userGetDetailVm.setFirstName("John");
        userGetDetailVm.setLastName("Doe");
        userGetDetailVm.setFullName("John Doe");
        userGetDetailVm.setHeadline("Software Developer");
        userGetDetailVm.setGender("male");
        userGetDetailVm.setActive(true);
        userGetDetailVm.setPhoto("photo_url");
        userGetDetailVm.setDateOfBirth("01-01-1990");
        userGetDetailVm.setRole("USER");
        userGetDetailVm.setNumberOfReview(10);
        userGetDetailVm.setNumberOfStudent(50);
        userGetDetailVm.setCourses(Collections.emptyList());

        // Mock the behavior of userService and courseService
        when(userService.getUserProfile(userId)).thenReturn(userGetDetailVm);
        when(courseService.getByUserId(userId)).thenReturn(Collections.emptyList());

        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userGetDetailVm.getId()))
                .andExpect(jsonPath("$.email").value(userGetDetailVm.getEmail()))
                .andExpect(jsonPath("$.firstName").value(userGetDetailVm.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userGetDetailVm.getLastName()))
                .andExpect(jsonPath("$.fullName").value(userGetDetailVm.getFullName()))
                .andExpect(jsonPath("$.headline").value(userGetDetailVm.getHeadline()))
                .andExpect(jsonPath("$.gender").value(userGetDetailVm.getGender()))
                .andExpect(jsonPath("$.active").value(userGetDetailVm.isActive()))
                .andExpect(jsonPath("$.photo").value(userGetDetailVm.getPhoto()))
                .andExpect(jsonPath("$.dateOfBirth").value(userGetDetailVm.getDateOfBirth()))
                .andExpect(jsonPath("$.role").value(userGetDetailVm.getRole()))
                .andExpect(jsonPath("$.numberOfReview").value(userGetDetailVm.getNumberOfReview()))
                .andExpect(jsonPath("$.numberOfStudent").value(userGetDetailVm.getNumberOfStudent()))
                .andExpect(jsonPath("$.courses").isEmpty());
    }

}
