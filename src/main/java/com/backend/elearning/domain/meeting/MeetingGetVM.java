package com.backend.elearning.domain.meeting;

import com.backend.elearning.domain.common.Event;
import com.backend.elearning.domain.common.EventType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@NoArgsConstructor
public class MeetingGetVM extends Event {

    private String code;
    private String startTime;
    private String endTime;

    public MeetingGetVM(Long id, EventType type, LocalDateTime createdAt, String code, String startTime, String endTime) {
        super(id, type, createdAt);
        this.code = code;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public MeetingGetVM(String code, String startTime, String endTime) {
        this.code = code;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
