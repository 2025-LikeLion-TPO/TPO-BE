package org.example.tpo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.tpo.dto.event.response.EventListWrapperResponse;
import org.example.tpo.entity.Users;
import org.example.tpo.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "Calendar", description = "캘린더 조회 API")
@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final EventService eventService;

    @Operation(
            summary = "월별 캘린더 조회",
            description = "연도와 월을 기준으로 해당 월의 모든 이벤트 조회"
    )
    @GetMapping("/calendar/month")
    public EventListWrapperResponse getMonthlyCalendar(
            @RequestParam int year,
            @RequestParam int month,
            Authentication authentication
    ) {
        Users user = (Users) authentication.getPrincipal();
        return eventService.getMonthlyCalendar(user, year, month);
    }

    @Operation(
            summary = "일별 캘린더 조회",
            description = "특정 날짜에 해당하는 이벤트 목록 조회"
    )
    @GetMapping("/calendar/day")
    public EventListWrapperResponse getDailyCalendar(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            Authentication authentication
    ) {
        Users user = (Users) authentication.getPrincipal();
        return eventService.getDailyCalendar(user, date);
    }
}
