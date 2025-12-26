package org.example.tpo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tpo.dto.event.request.EventAlarmUpdateRequest;
import org.example.tpo.dto.event.request.EventCreateRequest;
import org.example.tpo.dto.event.request.EventUpdateRequest;
import org.example.tpo.dto.event.response.ContactEventListWrapperResponse;
import org.example.tpo.dto.event.response.EventGuideResponse;
import org.example.tpo.dto.event.response.EventListWrapperResponse;
import org.example.tpo.entity.Users;
import org.example.tpo.service.EventGuideService;
import org.example.tpo.service.EventService;
import org.springframework.web.bind.annotation.*;
import org.example.tpo.dto.event.response.EventDetailResponse;

// ✅ 로그인 유저 주입 방식에 맞게 수정 필요
// 예: @AuthenticationPrincipal, @LoginUser, 세션 등
@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventGuideService eventGuideService;

    @GetMapping("/events")
    public EventListWrapperResponse getEvents(Users user) {
        return eventService.getEvents(user);
    }

    @GetMapping("/contacts/{contactId}/events")
    public ContactEventListWrapperResponse getEventsByContact(
            @PathVariable Long contactId,
            Users user   // ⚠️ 프로젝트 로그인 유저 주입 방식에 맞게 변경
    ) {
        return eventService.getEventsByContact(user, contactId);
    }

    @GetMapping("/events/{eventId}")
    public EventDetailResponse getEventDetail(
            @PathVariable Long eventId,
            Users user // ⚠️ 프로젝트 로그인 유저 주입 방식에 맞게 변경
    ) {
        return eventService.getEventDetail(user, eventId);
    }

    @GetMapping("/events/today")
    public EventListWrapperResponse getTodayEvents(
            Users user // ⚠️ 로그인 유저 주입 방식에 맞게
    ) {
        return eventService.getTodayEvents(user);
    }

    @GetMapping("/events/upcoming")
    public EventListWrapperResponse getUpcomingEvents(
            Users user // ⚠️ 로그인 유저 주입 방식에 맞게
    ) {
        return eventService.getUpcomingEvents(user);
    }

    @PostMapping("/events")
    public Long createEvent(
            @RequestParam Long contactId,
            @RequestBody @Valid EventCreateRequest request,
            Users user // ⚠️ 로그인 유저 주입 방식에 맞게
    ) {
        return eventService.createEvent(user, contactId, request);
    }

    @PutMapping("/events/{eventId}")
    public void updateEvent(
            @PathVariable Long eventId,
            @RequestBody @Valid EventUpdateRequest request,
            Users user // ⚠️ 로그인 유저 주입 방식에 맞게
    ) {
        eventService.updateEvent(user, eventId, request);
    }

    @PostMapping("/events/{eventId}/complete")
    public void completeEvent(
            @PathVariable Long eventId,
            Users user // ⚠️ 로그인 유저 주입 방식에 맞게
    ) {
        eventService.completeEvent(user, eventId);
    }

    @PutMapping("/events/{eventId}/alarm")
    public void updateEventAlarm(
            @PathVariable Long eventId,
            @RequestBody @Valid EventAlarmUpdateRequest request,
            Users user // ⚠️ 로그인 유저 주입 방식에 맞게
    ) {
        eventService.updateEventAlarm(user, eventId, request.getNotificationEnabled());
    }

    @DeleteMapping("/events/{eventId}")
    public void deleteEvent(
            @PathVariable Long eventId,
            Users user // ⚠️ 로그인 유저 주입 방식에 맞게
    ) {
        eventService.deleteEvent(user, eventId);
    }

    @GetMapping("/events/{eventId}/guide")
    public EventGuideResponse getEventGuide(
            @PathVariable Long eventId,
            Users user
    ) {
        return eventGuideService.generateGuide(user, eventId);
    }

}
