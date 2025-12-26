package org.example.tpo.dto.event.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.example.tpo.entity.Event;
import org.example.tpo.entity.EventType;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateRequest {

    @NotBlank
    private String eventTitle;

    @NotNull
    private EventType eventType;   // ✅ EventType 엔티티 그대로 사용

    @NotNull
    private LocalDate eventDate;

    @NotNull
    private Boolean notificationEnabled;

    private String eventMemo;

    @NotNull
    private Event.EventStatus eventStatus; // ✅ enum
}
