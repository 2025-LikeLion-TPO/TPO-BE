package org.example.tpo.service;

import lombok.RequiredArgsConstructor;
import org.example.tpo.entity.Contact;
import org.example.tpo.entity.Event;
import org.example.tpo.entity.Users;
import org.example.tpo.repository.ContactRepository;
import org.example.tpo.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.tpo.dto.event.request.EventCreateRequest;


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
}
