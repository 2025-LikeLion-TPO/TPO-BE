package org.example.tpo.controller;

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

@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final EventService eventService;

    /**
     * 월별 캘린더 조회
     */
    @GetMapping("/calendar/month")
    public EventListWrapperResponse getMonthlyCalendar(
            @RequestParam int year,
            @RequestParam int month,
            Authentication authentication
    ) {
        Users user = (Users) authentication.getPrincipal();
        return eventService.getMonthlyCalendar(user, year, month);
    }

    /**
     * 일별 캘린더 조회
     */
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
