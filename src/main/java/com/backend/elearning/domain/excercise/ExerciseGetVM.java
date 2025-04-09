package com.backend.elearning.domain.excercise;

import com.backend.elearning.domain.common.Event;
import com.backend.elearning.domain.common.EventType;
import com.backend.elearning.domain.exerciseFile.ExerciseFileVM;
import com.backend.elearning.domain.referencefile.ReferenceFileVM;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Setter
@Getter
@NoArgsConstructor
public class ExerciseGetVM extends Event {
    String title;

    String description;

    String deadline;

    List<ExerciseFileVM> files;

    public ExerciseGetVM(Long id, EventType type, LocalDateTime createdAt, String title, String description, String deadline, List<ExerciseFileVM> files) {
        super(id, type, createdAt);
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.files = files;
    }

    public ExerciseGetVM(String title, String description, String deadline, List<ExerciseFileVM> files) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.files = files;
    }
}
