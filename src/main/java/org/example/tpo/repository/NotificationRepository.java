package org.example.tpo.repository;

import org.example.tpo.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByEvent_EventId(Long eventId);
}
