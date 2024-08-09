package com.backend.elearning.domain.course;

import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.common.AbstractAuditEntity;
import com.backend.elearning.domain.common.CustomAuditingEntityListener;
import com.backend.elearning.domain.learning.learningCourse.LearningCourse;
import com.backend.elearning.domain.review.Review;
import com.backend.elearning.domain.section.Section;
import com.backend.elearning.domain.topic.Topic;
import com.backend.elearning.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(value = CustomAuditingEntityListener.class)
public class Course extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String title;

    private String slug;

    private String headline;

    private String[] objectives;

    private String[] requirements;

    private String[] targetAudiences;

    @Column(length = 1000)
    private String description;

    private String imageId;

    @Enumerated(EnumType.STRING)
    private ELevel level;

    private boolean free;

    private boolean publish;

    private Long price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Section> sections = new ArrayList<>();


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LearningCourse> learningCourses = new ArrayList<>();
}
