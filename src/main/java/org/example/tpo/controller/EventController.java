package org.example.tpo.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.tpo.common.response.BaseResponse;
import org.example.tpo.dto.event.request.EventCreateRequestDto;
import org.example.tpo.dto.event.request.EventUpdateRequestDto;
import org.example.tpo.dto.event.response.EventResponseDto;
import org.example.tpo.dto.event.response.UpcomingEventResponseDto;
import org.example.tpo.entity.Event;
import org.example.tpo.service.EventService;
import org.example.tpo.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final JwtUtil jwtUtil;

    private Long extractUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization")
                .replace("Bearer ", "");
        return jwtUtil.getUserIdFromToken(token);
    }

    /* 월별 캘린더 */
    @GetMapping("/month")
    public BaseResponse<List<EventResponseDto>> getMonthlyEvents(
            HttpServletRequest request,
            @RequestParam int year,
            @RequestParam int month
    ) {
        Long userId = extractUserId(request);

        return BaseResponse.success(
                eventService.getMonthlyEvents(userId, year, month)
                        .stream()
                        .map(EventResponseDto::from)
                        .toList()
        );
    }

    /* 일별 캘린더 */
    @GetMapping("/day")
    public BaseResponse<List<EventResponseDto>> getDailyEvents(
            HttpServletRequest request,
            @RequestParam LocalDate date
    ) {
        Long userId = extractUserId(request);

        return BaseResponse.success(
                eventService.getDailyEvents(userId, date)
                        .stream()
                        .map(EventResponseDto::from)
                        .toList()
        );
    }

    /* 다가오는 이벤트 */
    @GetMapping("/upcoming")
    public BaseResponse<List<UpcomingEventResponseDto>> getUpcomingEvents(
            HttpServletRequest request
    ) {
        Long userId = extractUserId(request);

        return BaseResponse.success(
                eventService.getUpcomingEvents(userId)
                        .stream()
                        .map(UpcomingEventResponseDto::from)
                        .toList()
        );
    }

    /* 이벤트 생성 */
    @PostMapping
    public BaseResponse<EventResponseDto> createEvent(
            HttpServletRequest request,
            @RequestBody EventCreateRequestDto dto
    ) {
        Long userId = extractUserId(request);

        Event event = eventService.createEvent(userId, dto);
        return BaseResponse.success(EventResponseDto.from(event));
    }

    /* 이벤트 수정 */
    @PutMapping("/{eventId}")
    public BaseResponse<EventResponseDto> updateEvent(
            @PathVariable Long eventId,
            @RequestBody EventUpdateRequestDto dto
    ) {
        Event event = eventService.updateEvent(eventId, dto);
        return BaseResponse.success(EventResponseDto.from(event));
    }

    /* 이벤트 삭제 */
    @DeleteMapping("/{eventId}")
    public BaseResponse<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return BaseResponse.success(null);
    }

    /* 이벤트 완료 */
    @PostMapping("/{eventId}/complete")
    public BaseResponse<Void> completeEvent(@PathVariable Long eventId) {
        eventService.completeEvent(eventId);
        return BaseResponse.success(null);
    }
}
