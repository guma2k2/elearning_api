package com.backend.elearning.domain.note;

import com.backend.elearning.domain.lecture.Lecture;
import com.backend.elearning.domain.student.Student;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "note")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(length = 500)
    private String content;

    private int time;
}
