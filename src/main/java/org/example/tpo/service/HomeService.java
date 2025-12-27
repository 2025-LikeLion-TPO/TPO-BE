package org.example.tpo.service;

import lombok.RequiredArgsConstructor;
import org.example.tpo.dto.home.response.*;
import org.example.tpo.entity.Contact;
import org.example.tpo.entity.Event;
import org.example.tpo.entity.Users;
import org.example.tpo.repository.ContactRepository;
import org.example.tpo.repository.EventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

import org.example.tpo.dto.home.response.HomeResponse;
import org.example.tpo.dto.home.response.RecentContactDto;
import org.example.tpo.dto.home.response.TodayEventDto;


@Service
@RequiredArgsConstructor
public class HomeService {

    private final EventRepository eventRepository;

    public HomeResponse getHome(Users user) {
        if (user == null) throw new IllegalArgumentException("로그인한 유저 정보가 필요합니다.");

        // ===== 1) recentContacts (최근 이벤트 기준) =====
        int limit = 5;

        // 이벤트는 중복 contact가 많으니 넉넉히 30개 정도 뽑아서 중복 제거
        List<Event> latestEvents = eventRepository.findByUserOrderByEventDateDescEventIdDesc(
                user, PageRequest.of(0, 30)
        );

        LinkedHashMap<Long, Contact> dedup = new LinkedHashMap<>();
        for (Event e : latestEvents) {
            Contact c = e.getContact();
            dedup.putIfAbsent(c.getContactId(), c);
            if (dedup.size() >= limit) break;
        }

        List<RecentContactDto> recentContacts = dedup.values().stream()
                .map(c -> new RecentContactDto(
                        c.getContactId(),
                        c.getContactName(),
                        c.getTemperature(),
                        c.getRelationshipType(),
                        c.getGiveCount(),
                        c.getReceiveCount()
                ))
                .toList();

        // ===== 2) todayEvents (오늘 날짜 동일) =====
        LocalDate today = LocalDate.now();
        List<TodayEventDto> todayEvents = eventRepository
                .findByUserAndEventDateAndEventStatusNotOrderByEventDateAsc(
                        user, today, Event.EventStatus.CANCELED
                )
                .stream()
                .map(e -> new TodayEventDto(
                        e.getEventId(),
                        e.getEventTitle(),
                        e.getEventDate(),
                        e.getContact().getContactName(),
                        e.getContact().getRelationshipType(),
                        e.getContact().getTemperature()
                ))
                .toList();

        // userName: 너희 Users 구조에 맞춰 선택
        String userName = (user.getNickname() != null && !user.getNickname().isBlank())
                ? user.getNickname()
                : user.getUsername();

        return new HomeResponse(userName, recentContacts, todayEvents);
    }
}

