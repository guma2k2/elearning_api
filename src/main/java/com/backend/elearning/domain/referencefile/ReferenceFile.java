package com.backend.elearning.domain.referencefile;

import com.backend.elearning.domain.classroom.Classroom;
import com.backend.elearning.reference.Reference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reference_file")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ReferenceFile  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_id")
    private Reference reference;
}
