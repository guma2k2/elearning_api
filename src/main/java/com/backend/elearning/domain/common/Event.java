package com.backend.elearning.domain.common;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Event {

    private Long id;

    private EventType type;

    private LocalDateTime createdAt;

}
