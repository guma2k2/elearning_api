package com.backend.elearning.domain.topic;

import com.backend.elearning.domain.common.CustomAuditingEntityListener;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "topic")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(value = CustomAuditingEntityListener.class)
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;



}
