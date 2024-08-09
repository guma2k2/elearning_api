package com.backend.elearning.domain.user;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.course.CourseListGetVM;
import com.backend.elearning.domain.course.CourseService;
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
public class UserController {

    private final UserService userService;

    private final CourseService courseService;

    public UserController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/admin/users/{id}")
    public ResponseEntity<UserGetDetailVm> getUser (@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserGetDetailVm> getUserProfile (@PathVariable("id") Long id) {
        UserGetDetailVm userProfile = userService.getUserProfile(id);
        List<CourseListGetVM> courseListGetVMS = courseService.getByUserId(id);
        userProfile.setCourses(courseListGetVMS);
        return ResponseEntity.ok().body(userProfile);
    }

    @GetMapping("/admin/users/paging")
    public ResponseEntity<PageableData<UserVm>> getUser (
            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        return ResponseEntity.ok().body(userService.getUsers(pageNum, pageSize, keyword));
    }

    @PostMapping({"/admin/users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
                @Content(schema = @Schema(implementation = UserVm.class))),
            @ApiResponse(responseCode = "409", description = "Duplicated email", content =
                @Content(schema = @Schema(implementation = ErrorVm.class))),
    })
    public ResponseEntity<UserVm> create (@RequestBody UserPostVm userPostVm) {
        UserVm savedUser = userService.create(userPostVm);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping({"/admin/users/{id}"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found", content =
                @Content(schema = @Schema(implementation = ErrorVm.class))),
            @ApiResponse(responseCode = "409", description = "Duplicated email", content =
                @Content(schema = @Schema(implementation = ErrorVm.class)))
    })
    public ResponseEntity<UserVm> update (@RequestBody UserPutVm userPostVm,
                                          @PathVariable("id") Long id
    ) {
        UserVm userVm = userService.update(userPostVm, id);
        return ResponseEntity.ok().body(userVm);
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<Void> delete (
            @PathVariable("id") Long id
    ) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
