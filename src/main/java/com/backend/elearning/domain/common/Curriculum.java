package com.backend.elearning.domain.common;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Curriculum {
    private Long id;
    private String title;

    private float number;

    private ECurriculumType type;

    private String updatedAt;



    public Curriculum(Long id, String title, float number, ECurriculumType type) {
        this.id = id;
        this.title = title;
        this.number = number;
        this.type = type;
    }
}
