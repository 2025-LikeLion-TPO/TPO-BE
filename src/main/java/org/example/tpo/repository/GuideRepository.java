package org.example.tpo.repository;

import org.example.tpo.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuideRepository extends JpaRepository<Guide, Long> {

    Optional<Guide> findByEventTypeAndRelationshipType(
            Guide.EventType eventType,
            Guide.RelationshipType relationshipType
    );
}
