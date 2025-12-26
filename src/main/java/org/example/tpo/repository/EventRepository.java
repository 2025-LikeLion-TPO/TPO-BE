package org.example.tpo.repository;

import org.example.tpo.entity.Event;
import org.example.tpo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // 유저 기준 전체 이벤트
    List<Event> findByUser(Users user);

    // 날짜 기준 이벤트
    List<Event> findByUserAndEventDate(Users user, LocalDate eventDate);

    // 월별 이벤트
    List<Event> findByUserAndEventDateBetween(
            Users user,
            LocalDate startDate,
            LocalDate endDate
    );

    // 다가오는 이벤트
    List<Event> findByUserAndEventStatusAndEventDateAfter(
            Users user,
            Event.EventStatus status,
            LocalDate date
    );
}
