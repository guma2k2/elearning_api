package com.backend.elearning.domain.student;


import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.user.UserVm;

import java.util.List;

public interface StudentService {
    PageableData<StudentListGetVM> getStudents(int pageNum, int pageSize, String keyword);

    void updateStatusStudent(boolean status, Long studentId);

    UserVm updateProfileStudent(StudentPutVM studentPutVM);
}
