package com.backend.elearning.reference;

import com.backend.elearning.domain.common.Event;
import com.backend.elearning.domain.common.EventType;
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
public class ReferenceGetVM extends Event {
    String description;
    List<ReferenceFileVM> files;

    public ReferenceGetVM(Long id, EventType type, LocalDateTime createdAt, String description, List<ReferenceFileVM> files) {
        super(id, type, createdAt);
        this.description = description;
        this.files = files;
    }

    public ReferenceGetVM(String description, List<ReferenceFileVM> files) {
        this.description = description;
        this.files = files;
    }
}
