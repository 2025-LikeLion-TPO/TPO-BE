package org.example.tpo.repository;

import org.example.tpo.entity.Contact;
import org.example.tpo.entity.Event;
import org.example.tpo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByContactOrderByEventDateDesc(Contact contact);
    List<Event> findByUserOrderByEventDateAsc(Users user);
    List<Event> findByUserAndEventDateOrderByEventDateAsc(
            Users user,
            LocalDate eventDate
    );
    List<Event> findByUserAndEventDateAfterAndEventStatusOrderByEventDateAsc(
            Users user,
            LocalDate date,
            Event.EventStatus eventStatus
    );
    List<Event> findByUserAndEventDateBetweenOrderByEventDateAsc(
            Users user,
            LocalDate startDate,
            LocalDate endDate
    );

    long countByContactAndEventStatus(Contact contact, Event.EventStatus eventStatus);
}
