package com.backend.elearning.domain.excercise;

import com.backend.elearning.domain.classroom.Classroom;
import com.backend.elearning.domain.common.AbstractAuditEntity;
import com.backend.elearning.domain.exerciseFile.ExerciseFile;
import com.backend.elearning.domain.reference.Reference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercise")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Exercise extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private LocalDateTime submission_deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ExerciseFile> exerciseFiles = new ArrayList<>();

}
