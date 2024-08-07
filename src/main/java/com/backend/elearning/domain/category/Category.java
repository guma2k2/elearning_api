package com.backend.elearning.domain.category;

import com.backend.elearning.domain.common.AbstractAuditEntity;
import com.backend.elearning.domain.common.CustomAuditingEntityListener;
import com.backend.elearning.domain.course.Course;
import com.backend.elearning.domain.topic.Topic;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "category")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(value = CustomAuditingEntityListener.class)
public class Category extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 40,unique = true)
    private String name;

    private String description;

    private boolean publish;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Category> childrenList = new ArrayList<>();


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Course> courses = new ArrayList<>();

    @ManyToMany(mappedBy = "categories")
    @Builder.Default
    private Set<Topic> topics = new HashSet<>();

    public Category(String name) {
        this.name = name;
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
        topic.getCategories().add(this);
    }

    public void removeTopic(Topic topic) {
        topics.remove(topic);
        topic.getCategories().remove(this);
    }


}
