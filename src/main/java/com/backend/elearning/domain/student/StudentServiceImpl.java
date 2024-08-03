package com.backend.elearning.domain.student;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.user.User;
import com.backend.elearning.domain.user.UserVm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public PageableData<StudentListGetVM> getStudents(int pageNum, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Student> studentPage = studentRepository.findAllCustom(pageable, keyword);
        List<Student> students = studentPage.getContent();
        List<StudentListGetVM> studentListGetVMS = students.stream().map((student) -> StudentListGetVM.fromModelStudent(student)).toList();
        return new PageableData<>(
                pageNum,
                pageSize,
                (int) studentPage.getTotalElements(),
                studentPage.getTotalPages(),
                studentListGetVMS
        );
    }

    @Override
    @Transactional
    public void updateStatusStudent(boolean status, Long studentId) {
        studentRepository.updateStatusStudent(status, studentId);
    }
}
