package com.backend.elearning.controller;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.topic.TopicController;
import com.backend.elearning.domain.topic.TopicPostVM;
import com.backend.elearning.domain.topic.TopicService;
import com.backend.elearning.domain.topic.TopicVM;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = TopicController.class,
        excludeAutoConfiguration = {
                UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class
        }
)
public class TopicControllerTest {

    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TopicService topicService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getPageableTopic_ShouldReturnPagedTopics_WhenValidParams() throws Exception {
        // Given
        int pageNum = 1;
        int pageSize = 10;
        String keyword = "test";
        PageableData<TopicVM> pageableData = new PageableData<>();
        pageableData.setPageNum(pageNum);
        pageableData.setPageSize(pageSize);
        pageableData.setTotalElements(1L);
        pageableData.setTotalPages(1);
        pageableData.setContent(Collections.singletonList(new TopicVM(1, "Test Topic", "Description", true, List.of("Category1"), "2024-08-29", "2024-08-29")));

        when(topicService.getPageableTopics(pageNum, pageSize, keyword)).thenReturn(pageableData);

        // When & Then
        mockMvc.perform(get("/api/v1/admin/topics/paging")
                        .param("pageNum", String.valueOf(pageNum))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNum").value(pageNum))
                .andExpect(jsonPath("$.pageSize").value(pageSize))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test Topic"))
                .andExpect(jsonPath("$.content[0].description").value("Description"))
                .andExpect(jsonPath("$.content[0].isPublish").value(true))
                .andExpect(jsonPath("$.content[0].categories[0]").value("Category1"))
                .andExpect(jsonPath("$.content[0].createdAt").value("2024-08-29"))
                .andExpect(jsonPath("$.content[0].updatedAt").value("2024-08-29"));
    }

    @Test
    void create_ShouldReturnCreatedTopic_WhenValidInput() throws Exception {
        // Given
        TopicPostVM topicPostVM = new TopicPostVM(
                null,
                "New Topic",
                "New Description",
                true,
                List.of("Category1", "Category2")
        );
        TopicVM createdTopic = new TopicVM(1, "New Topic", "New Description", true, List.of("Category1", "Category2"), "2024-08-29", "2024-08-29");

        when(topicService.create(topicPostVM)).thenReturn(createdTopic);

        // When & Then
        mockMvc.perform(post("/api/v1/admin/topics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicPostVM)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Topic"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.isPublish").value(true))
                .andExpect(jsonPath("$.categories[0]").value("Category1"))
                .andExpect(jsonPath("$.categories[1]").value("Category2"))
                .andExpect(jsonPath("$.createdAt").value("2024-08-29"))
                .andExpect(jsonPath("$.updatedAt").value("2024-08-29"));
    }

    @Test
    void update_ShouldReturnNoContent_WhenValidInput() throws Exception {
        // Given
        Integer topicId = 1;
        TopicPostVM topicPostVM = new TopicPostVM(
                topicId,
                "Updated Topic",
                "Updated Description",
                false,
                List.of("Category3")
        );

        doNothing().when(topicService).update(topicPostVM, topicId);

        // When & Then
        mockMvc.perform(put("/api/v1/admin/topics/{id}", topicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicPostVM)))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_ShouldReturnNoContent_WhenTopicDeleted() throws Exception {
        // Given
        Integer topicId = 1;
        doNothing().when(topicService).delete(topicId);

        // When & Then
        mockMvc.perform(delete("/api/v1/admin/topics/{id}", topicId))
                .andExpect(status().isOk());
    }
}
