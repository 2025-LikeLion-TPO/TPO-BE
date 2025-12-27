package org.example.tpo.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "Event", description = "이벤트 관리 API")
@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventGuideService eventGuideService;

    // 🔹 공통: Authentication → Users 변환
    private Users getLoginUser(Authentication authentication) {
        return (Users) authentication.getPrincipal();
    }

    @Operation(summary = "이벤트 전체 조회",
            description = """
        - 로그인한 사용자의 전체 이벤트 목록을 조회합니다.
        - 과거 이벤트와 예정 이벤트가 모두 포함됩니다.
        """)
    @GetMapping("/events")
    public EventListWrapperResponse getEvents(Authentication authentication) {
        return eventService.getEvents(getLoginUser(authentication));
    }

    @Operation(
            summary = "지인별 이벤트 조회",
            description = """
            - 특정 지인(contact)에 연결된 이벤트 목록을 조회합니다.
            - 과거/예정 이벤트가 모두 포함됩니다.
            """
    )
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

    @Operation(
            summary = "이벤트 상세 조회",
            description = """
            - 단일 이벤트의 상세 정보를 조회합니다.
            - 가이드/추천 정보는 포함되지 않습니다.
            """
    )
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

    @Operation(summary = "오늘의 이벤트 조회")
    @GetMapping("/events/today")
    public EventListWrapperResponse getTodayEvents(Authentication authentication) {
        return eventService.getTodayEvents(getLoginUser(authentication));
    }

    @Operation(summary = "다가오는 이벤트 조회",
    description = """
        - 앞으로 다가오는 이벤트들을 조회합니다.
        """)
    @GetMapping("/events/upcoming")
    public EventListWrapperResponse getUpcomingEvents(Authentication authentication) {
        return eventService.getUpcomingEvents(getLoginUser(authentication));
    }

    @Operation(
            summary = "이벤트 생성",
            description = """
        - 지인(contact)에 대한 이벤트를 생성합니다.
        - 이벤트 유형은 event_type 테이블 기준입니다.
        """
    )
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

    @Operation(summary = "이벤트 수정")
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

    @Operation(
            summary = "이벤트 완료 처리",
            description = """
            - 이벤트를 완료 상태로 변경합니다.
            - 완료된 이벤트도 조회 API에는 포함됩니다.
            """
    )
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

    @Operation(summary = "이벤트 알림 설정 변경")
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

    @Operation(summary = "이벤트 삭제")
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

    @Operation(
            summary = "이벤트 가이드 조회",
            description = """
            - 이벤트를 기준으로 선물/금액/메시지 가이드를 반환합니다.
            - 해당 데이터는 서버에서 실시간 계산되며 DB에 저장되지 않습니다.
            - 화면 진입 시마다 호출해도 무방합니다.
            """
    )
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
