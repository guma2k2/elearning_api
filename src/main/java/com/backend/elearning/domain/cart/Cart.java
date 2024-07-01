package com.backend.elearning.domain.cart;

import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.student.Student;
import com.backend.elearning.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private boolean buyLater ;
}
