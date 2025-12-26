package org.example.tpo.dto.event.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ContactEventListWrapperResponse {
    private List<EventListResponse> events;
}

