package org.example.tpo.controller;

import lombok.RequiredArgsConstructor;
import org.example.tpo.dto.event.response.EventListWrapperResponse;
import org.example.tpo.entity.Users;
import org.example.tpo.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final EventService eventService;

    @GetMapping("/calendar/month")
    public EventListWrapperResponse getMonthlyCalendar(
            @RequestParam int year,
            @RequestParam int month,
            Users user // ⚠️ 로그인 유저 주입 방식에 맞게
    ) {
        return eventService.getMonthlyCalendar(user, year, month);
    }

    @GetMapping("/calendar/day")
    public EventListWrapperResponse getDailyCalendar(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            Users user // ⚠️ 로그인 유저 주입 방식에 맞게
    ) {
        return eventService.getDailyCalendar(user, date);
    }
}
