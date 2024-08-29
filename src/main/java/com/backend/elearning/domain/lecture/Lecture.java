package com.backend.elearning.domain.lecture;

import com.backend.elearning.domain.common.AbstractAuditEntity;
import com.backend.elearning.domain.common.CustomAuditingEntityListener;
import com.backend.elearning.domain.section.Section;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lecture")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EntityListeners(value = CustomAuditingEntityListener.class)
public class Lecture extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String title;

    private float number;

    private String videoId;

    private String lectureDetails;

    private int duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    public Lecture(LecturePostVM lecturePostVM, Section section) {
        this.title = lecturePostVM.title();
        this.number = lecturePostVM.number();
        this.videoId = lecturePostVM.video();
        this.lectureDetails = lecturePostVM.lectureDetails();
        this.duration = lecturePostVM.duration();
        this.section = section;
    }
}
