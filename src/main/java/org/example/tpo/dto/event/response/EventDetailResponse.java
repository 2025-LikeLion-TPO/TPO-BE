package org.example.tpo.dto.event.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class EventDetailResponse {

    private Long eventId;
    private Long contactId;

    private String eventTitle;
    private String eventType;

    private String eventStatus;
    private LocalDate eventDate;

    private Boolean notificationEnabled;
    private String eventMemo;
}
