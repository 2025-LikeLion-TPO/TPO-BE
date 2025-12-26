package org.example.tpo.repository;

import org.example.tpo.entity.Contact;
import org.example.tpo.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByContactOrderByEventDateDesc(Contact contact);

    long countByContactAndEventStatus(Contact contact, Event.EventStatus eventStatus);
}
