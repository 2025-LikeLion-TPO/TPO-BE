package org.example.tpo.service;

import lombok.RequiredArgsConstructor;
import org.example.tpo.dto.event.request.EventCreateRequestDto;
import org.example.tpo.dto.event.request.EventUpdateRequestDto;
import org.example.tpo.entity.Contact;
import org.example.tpo.entity.Event;
import org.example.tpo.entity.Users;
import org.example.tpo.repository.ContactRepository;
import org.example.tpo.repository.EventRepository;
import org.example.tpo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;

    private Users getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
    }

    private Contact getContact(Long contactId) {
        return contactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("지인 없음"));
    }

    /* 월별 캘린더 */
    public List<Event> getMonthlyEvents(Long userId, int year, int month) {
        Users user = getUser(userId);

        YearMonth ym = YearMonth.of(year, month);
        return eventRepository.findByUserAndEventDateBetween(
                user,
                ym.atDay(1),
                ym.atEndOfMonth()
        );
    }

    /* 일별 캘린더 */
    public List<Event> getDailyEvents(Long userId, LocalDate date) {
        return eventRepository.findByUserAndEventDate(getUser(userId), date);
    }

    /* 다가오는 이벤트 */
    public List<Event> getUpcomingEvents(Long userId) {
        return eventRepository.findByUserAndEventStatusAndEventDateAfter(
                getUser(userId),
                Event.EventStatus.SCHEDULED,
                LocalDate.now()
        );
    }

    /* 이벤트 상세 */
    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트 없음"));
    }

    /* 이벤트 생성 */
    @Transactional
    public Event createEvent(Long userId, EventCreateRequestDto request) {

        Event event = Event.builder()
                .user(getUser(userId))
                .contact(getContact(request.getContactId()))
                .eventTitle(request.getEventTitle())
                .eventType(request.getEventType())
                .eventDate(request.getEventDate())
                .eventMemo(request.getEventMemo())
                .eventStatus(Event.EventStatus.SCHEDULED)
                .build();

        return eventRepository.save(event);
    }

    /* 이벤트 수정 */
    @Transactional
    public Event updateEvent(Long eventId, EventUpdateRequestDto request) {
        Event event = getEvent(eventId);

        event.setEventTitle(request.getEventTitle());
        event.setEventType(request.getEventType());
        event.setEventDate(request.getEventDate());
        event.setEventMemo(request.getEventMemo());

        return event;
    }

    /* 이벤트 삭제 */
    @Transactional
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    /* 이벤트 완료 */
    @Transactional
    public void completeEvent(Long eventId) {
        Event event = getEvent(eventId);
        event.setEventStatus(Event.EventStatus.COMPLETED);
    }
}
