package com.backend.elearning.controller;

import com.backend.elearning.domain.cart.CartService;
import com.backend.elearning.domain.category.CategoryController;
import com.backend.elearning.domain.category.CategoryPostVM;
import com.backend.elearning.domain.category.CategoryService;
import com.backend.elearning.domain.category.CategoryVM;
import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.security.JWTUtil;
import com.backend.elearning.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = CategoryController.class,
        excludeAutoConfiguration = {
                UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class
        }
)
public class CategoryControllerTest {
    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CategoryService categoryService;


    @Test
    void getPageableCategory_ShouldReturnPageableDataOfCategoryVM_WhenDataIsAvailable() throws Exception {
        // Given
        int pageNum = 1;
        int pageSize = 10;
        String keyword = "test";

        List<CategoryVM> categories = List.of(
                new CategoryVM(1, "Category 1", "Description 1", true, "2024-08-01T12:00:00", "2024-08-01T12:00:00", 0),
                new CategoryVM(2, "Category 2", "Description 2", false, "2024-08-01T12:00:00", "2024-08-01T12:00:00", 1)
        );
        PageableData<CategoryVM> pageableData = new PageableData<>();
        pageableData.setPageNum(pageNum);
        pageableData.setPageSize(pageSize);
        pageableData.setTotalElements(2L);
        pageableData.setTotalPages(1);
        pageableData.setContent(categories);

        when(categoryService.getPageableCategories(pageNum, pageSize, keyword)).thenReturn(pageableData);

        // When & Then
        mockMvc.perform(get("/api/v1/admin/category/paging")
                        .param("pageNum", String.valueOf(pageNum))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("keyword", keyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements").value(pageableData.getTotalElements()))
                .andExpect(jsonPath("$.pageNum").value(pageableData.getPageNum()))
                .andExpect(jsonPath("$.pageSize").value(pageableData.getPageSize()))
                .andExpect(jsonPath("$.totalPages").value(pageableData.getTotalPages()))
                .andExpect(jsonPath("$.content[0].id").value(categories.get(0).id()))
                .andExpect(jsonPath("$.content[0].name").value(categories.get(0).name()))
                .andExpect(jsonPath("$.content[0].description").value(categories.get(0).description()))
                .andExpect(jsonPath("$.content[0].isPublish").value(categories.get(0).isPublish()))
                .andExpect(jsonPath("$.content[0].createdAt").value(categories.get(0).createdAt()))
                .andExpect(jsonPath("$.content[0].updatedAt").value(categories.get(0).updatedAt()))
                .andExpect(jsonPath("$.content[0].parentId").value(categories.get(0).parentId()))
                .andExpect(jsonPath("$.content[1].id").value(categories.get(1).id()))
                .andExpect(jsonPath("$.content[1].name").value(categories.get(1).name()))
                .andExpect(jsonPath("$.content[1].description").value(categories.get(1).description()))
                .andExpect(jsonPath("$.content[1].isPublish").value(categories.get(1).isPublish()))
                .andExpect(jsonPath("$.content[1].createdAt").value(categories.get(1).createdAt()))
                .andExpect(jsonPath("$.content[1].updatedAt").value(categories.get(1).updatedAt()))
                .andExpect(jsonPath("$.content[1].parentId").value(categories.get(1).parentId()));
    }

    @Test
    void create_ShouldReturnCreatedCategoryVM_WhenCategoryIsCreated() throws Exception {
        // Given
        CategoryPostVM categoryPostVM = new CategoryPostVM(null, "New Category", "New Description", true, null);
        CategoryVM categoryVM = new CategoryVM(1, "New Category", "New Description", true, "2024-08-01T12:00:00", "2024-08-01T12:00:00", 0);
        when(categoryService.create(any(CategoryPostVM.class))).thenReturn(categoryVM);

        // When & Then
        mockMvc.perform(post("/api/v1/admin/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryPostVM)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(categoryVM.id()))
                .andExpect(jsonPath("$.name").value(categoryVM.name()))
                .andExpect(jsonPath("$.description").value(categoryVM.description()))
                .andExpect(jsonPath("$.isPublish").value(categoryVM.isPublish()))
                .andExpect(jsonPath("$.createdAt").value(categoryVM.createdAt()))
                .andExpect(jsonPath("$.updatedAt").value(categoryVM.updatedAt()))
                .andExpect(jsonPath("$.parentId").value(categoryVM.parentId()));
    }

    @Test
    void update_ShouldReturnNoContent_WhenCategoryIsUpdated() throws Exception {
        // Given
        Integer categoryId = 1;
        CategoryPostVM categoryPostVM = new CategoryPostVM(categoryId, "Updated Category", "Updated Description", false, null);
        doNothing().when(categoryService).update(any(CategoryPostVM.class), Mockito.eq(categoryId));

        // When & Then
        mockMvc.perform(put("/api/v1/admin/category/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryPostVM)))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_ShouldReturnNoContent_WhenCategoryIsDeleted() throws Exception {
        // Given
        Integer categoryId = 1;
        doNothing().when(categoryService).delete(categoryId);

        // When & Then
        mockMvc.perform(delete("/api/v1/admin/category/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
