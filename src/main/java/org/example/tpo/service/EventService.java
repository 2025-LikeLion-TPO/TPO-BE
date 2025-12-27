package org.example.tpo.service;

import lombok.RequiredArgsConstructor;
import org.example.tpo.dto.event.request.EventUpdateRequest;
import org.example.tpo.dto.event.response.ContactEventListWrapperResponse;
import org.example.tpo.dto.event.response.EventDetailResponse;
import org.example.tpo.dto.event.response.EventListResponse;
import org.example.tpo.dto.event.response.EventListWrapperResponse;
import org.example.tpo.entity.Contact;
import org.example.tpo.entity.Event;
import org.example.tpo.entity.Users;
import org.example.tpo.repository.ContactRepository;
import org.example.tpo.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.tpo.dto.event.request.EventCreateRequest;

import java.time.YearMonth;
import java.util.List;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ContactRepository contactRepository;

    @Transactional
    public Long createEvent(Users user, Long contactId, EventCreateRequest request) {

        if (user == null) throw new IllegalArgumentException("로그인한 유저 정보가 필요합니다.");

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("지인 없음"));

        if (!user.getId().equals(contact.getUser().getId())) {
            throw new IllegalArgumentException("권한 없음");
        }

        Event event = Event.builder()
                .user(user)
                .contact(contact)
                .eventTitle(request.getEventTitle())
                .eventType(request.getEventType())               // ✅ EventType 엔티티
                .eventDate(request.getEventDate())
                .notificationEnabled(request.getNotificationEnabled())
                .eventMemo(request.getEventMemo())
                .eventStatus(request.getEventStatus())           // ✅ enum
                .build();

        eventRepository.save(event);

        // ✅ 생성 시 DONE이면 giveCount +1
        if (event.getEventStatus() == Event.EventStatus.DONE) {
            contact.setGiveCount(contact.getGiveCount() + 1);
        }

        return event.getEventId();
    }

    @Transactional(readOnly = true)
    public EventListWrapperResponse getEvents(Users user) {
        if (user == null) throw new IllegalArgumentException("로그인한 유저 정보가 필요합니다.");

        List<Event> events = eventRepository.findByUserOrderByEventDateAsc(user);

        List<EventListResponse> result = events.stream()
                .map(e -> new EventListResponse(
                        e.getEventId(),
                        e.getEventTitle(),
                        e.getEventType().getName(),
                        e.getEventDate(),
                        e.getEventMemo(),
                        e.getEventStatus().name()
                ))
                .toList();

        return new EventListWrapperResponse(result);
    }

    // ✅ 지인별 이벤트 조회 (/contacts/{contactId}/events)
    @Transactional(readOnly = true)
    public ContactEventListWrapperResponse getEventsByContact(Users user, Long contactId) {

        if (user == null) throw new IllegalArgumentException("로그인 필요");

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("지인 없음"));

        // 🔐 권한 체크
        if (!user.getId().equals(contact.getUser().getId())) {
            throw new IllegalArgumentException("권한 없음");
        }

        List<Event> events = eventRepository.findByContactOrderByEventDateDesc(contact);

        List<EventListResponse> result = events.stream()
                .map(e -> new EventListResponse(
                        e.getEventId(),
                        e.getEventTitle(),
                        e.getEventType().getName(),
                        e.getEventDate(),
                        e.getEventMemo(),
                        e.getEventStatus().name()
                ))
                .toList();

        return new ContactEventListWrapperResponse(result);
    }

    @Transactional(readOnly = true)
    public EventDetailResponse getEventDetail(Users user, Long eventId) {

        if (user == null) throw new IllegalArgumentException("로그인 필요");

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트 없음"));

        // 🔐 권한 체크: 이벤트 소유자 = 로그인 유저
        if (!user.getId().equals(event.getUser().getId())) {
            throw new IllegalArgumentException("권한 없음");
        }

        return new EventDetailResponse(
                event.getEventId(),
                event.getContact().getContactId(),
                event.getEventTitle(),
                event.getEventType().getName(),
                event.getEventStatus().name(),
                event.getEventDate(),
                event.getNotificationEnabled(),
                event.getEventMemo()
        );
    }

    @Transactional(readOnly = true)
    public EventListWrapperResponse getTodayEvents(Users user) {

        if (user == null) throw new IllegalArgumentException("로그인 필요");

        LocalDate today = LocalDate.now();

        List<Event> events =
                eventRepository.findByUserAndEventDateOrderByEventDateAsc(user, today);

        List<EventListResponse> result = events.stream()
                .map(e -> new EventListResponse(
                        e.getEventId(),
                        e.getEventTitle(),
                        e.getEventType().getName(),
                        e.getEventDate(),
                        e.getEventMemo(),
                        e.getEventStatus().name()
                ))
                .toList();

        return new EventListWrapperResponse(result);
    }

    @Transactional(readOnly = true)
    public EventListWrapperResponse getUpcomingEvents(Users user) {

        if (user == null) throw new IllegalArgumentException("로그인 필요");

        LocalDate today = LocalDate.now();

        List<Event> events =
                eventRepository.findByUserAndEventDateAfterAndEventStatusOrderByEventDateAsc(
                        user,
                        today,
                        Event.EventStatus.PLANNED
                );

        List<EventListResponse> result = events.stream()
                .map(e -> new EventListResponse(
                        e.getEventId(),
                        e.getEventTitle(),
                        e.getEventType().getName(),
                        e.getEventDate(),
                        e.getEventMemo(),
                        e.getEventStatus().name()
                ))
                .toList();

        return new EventListWrapperResponse(result);
    }

    @Transactional(readOnly = true)
    public EventListWrapperResponse getMonthlyCalendar(
            Users user,
            int year,
            int month
    ) {

        if (user == null) throw new IllegalArgumentException("로그인 필요");

        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Event> events =
                eventRepository.findByUserAndEventDateBetweenOrderByEventDateAsc(
                        user,
                        startDate,
                        endDate
                );

        List<EventListResponse> result = events.stream()
                .map(e -> new EventListResponse(
                        e.getEventId(),
                        e.getEventTitle(),
                        e.getEventType().getName(),
                        e.getEventDate(),
                        e.getEventMemo(),
                        e.getEventStatus().name()
                ))
                .toList();

        return new EventListWrapperResponse(result);
    }

    @Transactional(readOnly = true)
    public EventListWrapperResponse getDailyCalendar(
            Users user,
            LocalDate date
    ) {

        if (user == null) throw new IllegalArgumentException("로그인 필요");

        List<Event> events =
                eventRepository.findByUserAndEventDateOrderByEventDateAsc(user, date);

        List<EventListResponse> result = events.stream()
                .map(e -> new EventListResponse(
                        e.getEventId(),
                        e.getEventTitle(),
                        e.getEventType().getName(),
                        e.getEventDate(),
                        e.getEventMemo(),
                        e.getEventStatus().name()
                ))
                .toList();

        return new EventListWrapperResponse(result);
    }

    @Transactional
    public void updateEvent(
            Users user,
            Long eventId,
            EventUpdateRequest request
    ) {

        if (user == null) throw new IllegalArgumentException("로그인 필요");

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트 없음"));

        // 🔐 권한 체크
        if (!user.getId().equals(event.getUser().getId())) {
            throw new IllegalArgumentException("권한 없음");
        }

        event.setEventTitle(request.getEventTitle());
        event.setEventType(request.getEventType());
        event.setEventDate(request.getEventDate());
        event.setNotificationEnabled(request.getNotificationEnabled());
        event.setEventMemo(request.getEventMemo());
        event.setEventStatus(request.getEventStatus());
    }

    @Transactional
    public void completeEvent(Users user, Long eventId) {

        if (user == null) throw new IllegalArgumentException("로그인 필요");

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트 없음"));

        // 🔐 권한 체크
        if (!user.getId().equals(event.getUser().getId())) {
            throw new IllegalArgumentException("권한 없음");
        }

        // ✅ 이미 완료면 아무것도 안 함 (멱등)
        if (event.getEventStatus() == Event.EventStatus.DONE) {
            return;
        }

        // ✅ 완료 처리 + giveCount 증가
        event.setEventStatus(Event.EventStatus.DONE);

        Contact contact = event.getContact();
        contact.setGiveCount(contact.getGiveCount() + 1);
    }

    @Transactional
    public void updateEventAlarm(
            Users user,
            Long eventId,
            Boolean notificationEnabled
    ) {

        if (user == null) throw new IllegalArgumentException("로그인 필요");

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트 없음"));

        // 🔐 권한 체크
        if (!user.getId().equals(event.getUser().getId())) {
            throw new IllegalArgumentException("권한 없음");
        }

        event.setNotificationEnabled(notificationEnabled);
    }

    @Transactional
    public void deleteEvent(Users user, Long eventId) {

        if (user == null) throw new IllegalArgumentException("로그인 필요");

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트 없음"));

        // 🔐 권한 체크
        if (!user.getId().equals(event.getUser().getId())) {
            throw new IllegalArgumentException("권한 없음");
        }

        // ✅ 완료 이벤트 삭제 시 giveCount 감소
        if (event.getEventStatus() == Event.EventStatus.DONE) {
            Contact contact = event.getContact();
            contact.setGiveCount(Math.max(0, contact.getGiveCount() - 1));
        }

        eventRepository.delete(event);
    }

    /**
     * 🔔 오늘 알림 대상 이벤트 조회
     */
    @Transactional(readOnly = true)
    public EventListWrapperResponse getTodayNotificationEvents(Users user) {

        LocalDate today = LocalDate.now();

        List<Event> events = eventRepository
                .findByUserAndNotificationEnabledTrueAndEventStatusAndEventDate(
                        user,
                        Event.EventStatus.PLANNED,
                        today
                );

        return EventListWrapperResponse.from(events);
    }

    /**
     * 🔔 다가오는 알림 대상 이벤트 조회 (ex. 7일 이내)
     */
    @Transactional(readOnly = true)
    public EventListWrapperResponse getUpcomingNotificationEvents(
            Users user,
            int days
    ) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(days);

        List<Event> events = eventRepository
                .findByUserAndNotificationEnabledTrueAndEventStatusAndEventDateBetween(
                        user,
                        Event.EventStatus.PLANNED,
                        today.plusDays(1),
                        endDate
                );

        return EventListWrapperResponse.from(events);
    }


}
