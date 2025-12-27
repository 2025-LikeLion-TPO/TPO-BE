package org.example.tpo.dto.event.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.tpo.entity.Event;

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

    /**
     * Event 엔티티 → EventListResponse 변환
     */
    public static EventListResponse from(Event event) {
        return new EventListResponse(
                event.getEventId(),
                event.getEventTitle(),
                event.getEventType().getName(),
                event.getEventDate(),
                event.getEventMemo(),
                event.getEventStatus().name()
        );
    }
}
