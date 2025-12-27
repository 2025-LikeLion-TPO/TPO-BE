package org.example.tpo.dto.event.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.tpo.entity.Event;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class EventListWrapperResponse {

    private List<EventListResponse> events;

    /**
     * Event 엔티티 리스트를 EventListResponse 리스트로 변환
     */
    public static EventListWrapperResponse from(List<Event> events) {
        return new EventListWrapperResponse(
                events.stream()
                        .map(EventListResponse::from)
                        .collect(Collectors.toList())
        );
    }
}
