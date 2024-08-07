package com.backend.elearning.domain.category;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.user.UserVm;
import com.backend.elearning.exception.ErrorVm;
import com.backend.elearning.utils.Constants;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryVM> getCategoryById (
            @PathVariable("id") Integer categoryId
    ) {
        CategoryVM categoryVM = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok().body(categoryVM);
    }

    @GetMapping("/category/name/{name}")
    public ResponseEntity<CategoryGetVM> getCategoryByName (
            @PathVariable("name") String name
    ) {
        CategoryGetVM categoryGetVM = categoryService.getByName(name);
        return ResponseEntity.ok().body(categoryGetVM);
    }

    @GetMapping("/category/parents")
    public ResponseEntity<List<CategoryListGetVM>> getCategoryParents (
    ) {
        List<CategoryListGetVM> categoryListGetVMS = categoryService.getCategoryParents();
        return ResponseEntity.ok().body(categoryListGetVMS);
    }

    @GetMapping("/admin/category/paging")
    public ResponseEntity<PageableData<CategoryVM>> getPageableCategory (
            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword

    ) {
        PageableData<CategoryVM> pageableCategories = categoryService.getPageableCategories(pageNum, pageSize, keyword);
        return ResponseEntity.ok().body(pageableCategories);
    }

    @PostMapping("/admin/category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
            @Content(schema = @Schema(implementation = CategoryVM.class))),
            @ApiResponse(responseCode = "409", description = "Duplicated name", content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
    })
    public ResponseEntity<CategoryVM> create (
            @RequestBody CategoryPostVM categoryPostVM
    ) {
        CategoryVM categoryVM = categoryService.create(categoryPostVM);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryVM);
    }

    @PutMapping("/admin/category/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found", content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
            @ApiResponse(responseCode = "409", description = "Duplicated name", content =
            @Content(schema = @Schema(implementation = ErrorVm.class)))
    })
    public ResponseEntity<CategoryVM> update (
            @RequestBody CategoryPostVM categoryPostVM,
            @PathVariable("id") Integer id
    ) {
        categoryService.update(categoryPostVM, id);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/admin/category/{id}")
    public ResponseEntity<Void> delete (
            @PathVariable("id") Integer id
    ) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
