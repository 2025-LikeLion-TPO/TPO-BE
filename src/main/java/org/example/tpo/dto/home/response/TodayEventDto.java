package org.example.tpo.dto.home.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TodayEventDto {
    private Long eventId;
    private String eventTitle;
    private LocalDate eventDate;

    private String contactName;
    private String relationshipType;
    private Integer temperature;
}
