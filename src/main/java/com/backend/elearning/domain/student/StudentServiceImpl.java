package com.backend.elearning.domain.student;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.user.UserVm;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final PasswordEncoder passwordEncoder;
    public StudentServiceImpl(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PageableData<StudentListGetVM> getStudents(int pageNum, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Student> studentPage = keyword != null ? studentRepository.findAllWithKeyword(pageable, keyword) :
                studentRepository.findAll(pageable);
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

    @Override
    public UserVm updateProfileStudent(StudentPutVM studentPutVM) {
        if (studentRepository.countByExistedEmail(studentPutVM.email(), studentPutVM.id()) > 0) {
            throw new DuplicateException(Constants.ERROR_CODE.USER_EMAIL_DUPLICATED, studentPutVM.email());
        }
        Student student = studentRepository.findById(studentPutVM.id()).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.USER_NOT_FOUND, studentPutVM.id()));
        student.setEmail(studentPutVM.email());
        student.setFirstName(studentPutVM.firstName());
        student.setLastName(studentPutVM.lastName());
        if (studentPutVM.day() != null) {
            student.setDateOfBirth(LocalDate.of(studentPutVM.year(), studentPutVM.month(), studentPutVM.day()));
        }
        if (!studentPutVM.photo().isEmpty() && !studentPutVM.photo().isBlank()) {
            student.setPhoto(studentPutVM.photo());
        }
        if (!studentPutVM.password().isEmpty() && !studentPutVM.password().isBlank()) {
            student.setPassword(passwordEncoder.encode(studentPutVM.password()));
        }
        // update
        studentRepository.saveAndFlush(student);
        return UserVm.fromModelStudent(student);
    }
}
