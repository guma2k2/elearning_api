package com.backend.elearning.domain.student;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.review.ReviewPostVM;
import com.backend.elearning.domain.review.ReviewVM;
import com.backend.elearning.domain.user.UserVm;
import com.backend.elearning.utils.Constants;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping("/admin/students/paging")
    public ResponseEntity<PageableData<StudentListGetVM>> getStudents (
            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        return ResponseEntity.ok().body(studentService.getStudents(pageNum, pageSize, keyword));
    }
    @PutMapping("/admin/students/{id}/status/{status}")
    public ResponseEntity<ReviewVM> updateReview(@PathVariable("status") boolean status, @PathVariable("id") Long studentId){
        studentService.updateStatusStudent(status, studentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/students/profile")
    public ResponseEntity<UserVm> updateProfile(@RequestBody StudentPutVM studentPutVM) {
        UserVm userVm = studentService.updateProfileStudent(studentPutVM);
        return ResponseEntity.ok().body(userVm);
    }

}
