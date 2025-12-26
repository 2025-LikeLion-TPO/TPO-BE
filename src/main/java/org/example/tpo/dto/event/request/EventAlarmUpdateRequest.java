package org.example.tpo.dto.event.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventAlarmUpdateRequest {

    @NotNull
    private Boolean notificationEnabled;
}
