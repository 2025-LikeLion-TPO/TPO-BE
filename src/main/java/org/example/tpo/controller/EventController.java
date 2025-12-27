package org.example.tpo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tpo.dto.event.request.EventAlarmUpdateRequest;
import org.example.tpo.dto.event.request.EventCreateRequest;
import org.example.tpo.dto.event.request.EventUpdateRequest;
import org.example.tpo.dto.event.response.ContactEventListWrapperResponse;
import org.example.tpo.dto.event.response.EventDetailResponse;
import org.example.tpo.dto.event.response.EventGuideResponse;
import org.example.tpo.dto.event.response.EventListWrapperResponse;
import org.example.tpo.entity.Users;
import org.example.tpo.service.EventGuideService;
import org.example.tpo.service.EventService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventGuideService eventGuideService;

    // 🔹 공통: Authentication → Users 변환
    private Users getLoginUser(Authentication authentication) {
        return (Users) authentication.getPrincipal();
    }

    @GetMapping("/events")
    public EventListWrapperResponse getEvents(Authentication authentication) {
        return eventService.getEvents(getLoginUser(authentication));
    }

    @GetMapping("/contacts/{contactId}/events")
    public ContactEventListWrapperResponse getEventsByContact(
            @PathVariable Long contactId,
            Authentication authentication
    ) {
        return eventService.getEventsByContact(
                getLoginUser(authentication),
                contactId
        );
    }

    @GetMapping("/events/{eventId}")
    public EventDetailResponse getEventDetail(
            @PathVariable Long eventId,
            Authentication authentication
    ) {
        return eventService.getEventDetail(
                getLoginUser(authentication),
                eventId
        );
    }

    @GetMapping("/events/today")
    public EventListWrapperResponse getTodayEvents(Authentication authentication) {
        return eventService.getTodayEvents(getLoginUser(authentication));
    }

    @GetMapping("/events/upcoming")
    public EventListWrapperResponse getUpcomingEvents(Authentication authentication) {
        return eventService.getUpcomingEvents(getLoginUser(authentication));
    }

    @PostMapping("/events")
    public Long createEvent(
            @RequestParam Long contactId,
            @RequestBody @Valid EventCreateRequest request,
            Authentication authentication
    ) {
        return eventService.createEvent(
                getLoginUser(authentication),
                contactId,
                request
        );
    }

    @PutMapping("/events/{eventId}")
    public void updateEvent(
            @PathVariable Long eventId,
            @RequestBody @Valid EventUpdateRequest request,
            Authentication authentication
    ) {
        eventService.updateEvent(
                getLoginUser(authentication),
                eventId,
                request
        );
    }

    @PostMapping("/events/{eventId}/complete")
    public void completeEvent(
            @PathVariable Long eventId,
            Authentication authentication
    ) {
        eventService.completeEvent(
                getLoginUser(authentication),
                eventId
        );
    }

    @PutMapping("/events/{eventId}/alarm")
    public void updateEventAlarm(
            @PathVariable Long eventId,
            @RequestBody @Valid EventAlarmUpdateRequest request,
            Authentication authentication
    ) {
        eventService.updateEventAlarm(
                getLoginUser(authentication),
                eventId,
                request.getNotificationEnabled()
        );
    }

    @DeleteMapping("/events/{eventId}")
    public void deleteEvent(
            @PathVariable Long eventId,
            Authentication authentication
    ) {
        eventService.deleteEvent(
                getLoginUser(authentication),
                eventId
        );
    }

    @GetMapping("/events/{eventId}/guide")
    public EventGuideResponse getEventGuide(
            @PathVariable Long eventId,
            Authentication authentication
    ) {
        return eventGuideService.generateGuide(
                getLoginUser(authentication),
                eventId
        );
    }
}
