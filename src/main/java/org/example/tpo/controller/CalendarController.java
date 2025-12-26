package org.example.tpo.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.tpo.common.response.BaseResponse;
import org.example.tpo.dto.event.response.EventResponseDto;
import org.example.tpo.service.EventService;
import org.example.tpo.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {

    private final EventService eventService;
    private final JwtUtil jwtUtil;

    private Long extractUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization")
                .replace("Bearer ", "");
        return jwtUtil.getUserIdFromToken(token);
    }

    /**
     * 월별 캘린더 조회
     * GET /calendar/month?year=2025&month=7
     */
    @GetMapping("/month")
    public BaseResponse<List<EventResponseDto>> getMonthlyCalendar(
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

    /**
     * 일별 캘린더 조회
     * GET /calendar/day?date=2025-07-10
     */
    @GetMapping("/day")
    public BaseResponse<List<EventResponseDto>> getDailyCalendar(
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
}
