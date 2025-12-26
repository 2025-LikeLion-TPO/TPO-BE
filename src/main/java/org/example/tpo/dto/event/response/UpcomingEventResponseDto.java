package org.example.tpo.dto.event.response;

import lombok.*;
import org.example.tpo.entity.Event;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Builder
@AllArgsConstructor
public class UpcomingEventResponseDto {

    private Long eventId;
    private String eventTitle;
    private LocalDate eventDate;
    private long dDay;

    public static UpcomingEventResponseDto from(Event event) {
        return UpcomingEventResponseDto.builder()
                .eventId(event.getEventId())
                .eventTitle(event.getEventTitle())
                .eventDate(event.getEventDate())
                .dDay(ChronoUnit.DAYS.between(LocalDate.now(), event.getEventDate()))
                .build();
    }
}
