package com.backend.elearning.domain.reference;

import com.backend.elearning.domain.classroom.Classroom;
import com.backend.elearning.domain.common.AbstractAuditEntity;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.exerciseFile.ExerciseFile;
import com.backend.elearning.domain.referencefile.ReferenceFile;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reference")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Reference extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @OneToMany(mappedBy = "reference", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReferenceFile> referenceFiles = new ArrayList<>();

}
