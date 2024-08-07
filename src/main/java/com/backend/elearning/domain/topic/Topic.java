package com.backend.elearning.domain.topic;
import com.backend.elearning.domain.category.Category;
import com.backend.elearning.domain.common.AbstractAuditEntity;
import com.backend.elearning.domain.common.CustomAuditingEntityListener;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "topic")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(value = CustomAuditingEntityListener.class)
public class Topic extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 40,unique = true)
    private String name;

    private String description;

    private boolean publish;

    //Don’t use CascadeType.REMOVE with @ManyToMany associations:
    // https://vladmihalcea.com/orphanremoval-jpa-hibernate/
    @Builder.Default
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "category_topic",
            joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();


    public Topic(String name) {
        this.name = name;
    }

    public void addCategory(Category category) {
        if (category.getTopics() == null) {
            category.setTopics(new HashSet<>());
        }
        categories.add(category);
        category.getTopics().add(this);
    }
    public void removeCategory(Category category) {
        categories.remove(category);
        category.getTopics().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Topic topic)) return false;
        return Objects.equals(id, topic.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
