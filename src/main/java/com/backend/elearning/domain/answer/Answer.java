package com.backend.elearning.domain.answer;

import com.backend.elearning.domain.common.AbstractAuditEntity;
import com.backend.elearning.domain.common.CustomAuditingEntityListener;
import com.backend.elearning.domain.question.Question;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "answer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(value = CustomAuditingEntityListener.class)
public class Answer extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String answerText;
    private String reason;
    private boolean correct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question ;
}
