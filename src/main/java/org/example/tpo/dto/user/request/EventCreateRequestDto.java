package org.example.tpo.dto.event.request;

import lombok.*;
import org.example.tpo.entity.Event;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCreateRequestDto {

    private Long contactId;
    private String eventTitle;
    private Event.EventType eventType;
    private LocalDate eventDate;
    private String eventMemo;
}
