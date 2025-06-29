package com.backend.elearning.domain.user;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.course.CourseService;
import com.backend.elearning.domain.review.ReviewVM;
import com.backend.elearning.exception.ErrorVm;
import com.backend.elearning.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
public class UserController {

    private final UserService userService;

    private final CourseService courseService;

    public UserController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/admin/users/{id}")
    @Operation(method = "GET", summary = "Get information of user by id", description ="Send a request via API with role ADMIN to get information of user" )
    public ResponseEntity<UserGetDetailVm> getUser (@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @GetMapping("/users/{id}")
    @Operation(method = "GET", summary = "Get information of user by id", description ="Send a request via API to get information of user" )
    public ResponseEntity<UserGetDetailVm> getUserProfile (@PathVariable("id") Long id) {
        UserGetDetailVm userProfile = userService.getUserProfile(id);
        List<CourseListGetVM> courseListGetVMS = courseService.getByUserId(id);
        userProfile.setCourses(courseListGetVMS);
        return ResponseEntity.ok().body(userProfile);
    }

    @GetMapping("/admin/users/paging")
    @Operation(method = "GET", summary = "Get list information of user by pageNum, pageSize, keyword", description ="Send a request via API to get information of user" )
    public ResponseEntity<PageableData<UserVm>> getUser (
            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        return ResponseEntity.ok().body(userService.getUsers(pageNum, pageSize, keyword));
    }

    @PostMapping("/admin/users")
    @Operation(method = "POST", summary = "Save information of user", description ="Send a request via API to save user" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
                @Content(schema = @Schema(implementation = UserVm.class))),
            @ApiResponse(responseCode = "409", description = "Duplicated email", content =
                @Content(schema = @Schema(implementation = ErrorVm.class))),
    })
    public ResponseEntity<UserVm> create (@Valid @RequestBody UserPostVm userPostVm) {
        UserVm savedUser = userService.create(userPostVm);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/admin/users/{id}")
    @Operation(method = "PUT", summary = "Update information of user by id", description ="Send a request via API to update user" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found", content =
                @Content(schema = @Schema(implementation = ErrorVm.class))),
            @ApiResponse(responseCode = "409", description = "Duplicated email", content =
                @Content(schema = @Schema(implementation = ErrorVm.class)))
    })
    public ResponseEntity<UserVm> update (@Valid @RequestBody UserPutVm userPostVm,
                                          @PathVariable("id") Long id
    ) {
        UserVm userVm = userService.update(userPostVm, id);
        return ResponseEntity.ok().body(userVm);
    }

    @DeleteMapping("/admin/users/{id}")
    @Operation(method = "DELETE", summary = "Delete information of user by id", description ="Send a request via API by ROLE ADMIN to delete user" )
    public ResponseEntity<Void> delete (
            @PathVariable("id") Long id
    ) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/users/{id}/status/{status}")
    @Operation(method = "PUT", summary = "Update status of user by id", description ="Send a request via API to update status of user" )
    public ResponseEntity<ReviewVM> updateReview(@PathVariable("status") boolean status, @PathVariable("id") Long userId){
        userService.updateStatus(status, userId);
        return ResponseEntity.noContent().build();
    }

}
