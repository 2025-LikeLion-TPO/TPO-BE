package org.example.tpo.dto.event.response;

import lombok.*;
import org.example.tpo.entity.Event;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class EventResponseDto {

    private Long eventId;
    private String eventTitle;
    private Event.EventType eventType;
    private Event.EventStatus eventStatus;
    private LocalDate eventDate;
    private String eventMemo;

    public static EventResponseDto from(Event event) {
        return EventResponseDto.builder()
                .eventId(event.getEventId())
                .eventTitle(event.getEventTitle())
                .eventType(event.getEventType())
                .eventStatus(event.getEventStatus())
                .eventDate(event.getEventDate())
                .eventMemo(event.getEventMemo())
                .build();
    }
}
