package org.example.tpo.dto.event.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class EventListResponse {

    private Long eventId;
    private String eventTitle;

    // EventType 엔티티 → 프론트에는 이름만 내려줌
    private String eventType;

    private LocalDate eventDate;
    private String eventMemo;
    private String eventStatus; // enum → String
}
