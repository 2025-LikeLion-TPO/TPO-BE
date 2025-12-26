package org.example.tpo.repository;

import org.example.tpo.entity.Contact;
import org.example.tpo.entity.Event;
import org.example.tpo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // contact 상세(지난 이벤트)
    List<Event> findByContactOrderByEventDateDesc(Contact contact);

    // giveCount 계산/검증용
    long countByContactAndEventStatus(Contact contact, Event.EventStatus eventStatus);

    // ✅ 홈 - 오늘 이벤트 (취소 포함)
    List<Event> findByUserAndEventDateOrderByEventDateAsc(Users user, LocalDate eventDate);

    // ✅ 홈 - 오늘 이벤트 (취소 제외 추천)
    List<Event> findByUserAndEventDateAndEventStatusNotOrderByEventDateAsc(
            Users user, LocalDate eventDate, Event.EventStatus status
    );

    // ✅ recentContacts용: 유저 이벤트 최신순 (eventDate DESC, eventId DESC)
    List<Event> findByUserOrderByEventDateDescEventIdDesc(Users user, Pageable pageable);
}
