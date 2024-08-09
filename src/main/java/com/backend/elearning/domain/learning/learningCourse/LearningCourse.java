package com.backend.elearning.domain.learning.learningCourse;


import com.backend.elearning.domain.common.CustomAuditingEntityListener;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "learning_course")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LearningCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

}
