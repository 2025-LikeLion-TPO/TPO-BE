package org.example.tpo.service;

import lombok.RequiredArgsConstructor;
import org.example.tpo.entity.Event;
import org.example.tpo.entity.Guide;
import org.example.tpo.repository.GuideRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuideService {

    private final GuideRepository guideRepository;

    public Guide getGuide(
            Event.EventType eventType,
            Guide.RelationshipType relationshipType
    ) {
        return guideRepository.findByEventTypeAndRelationshipType(
                eventType, relationshipType
        ).orElseThrow(() -> new IllegalArgumentException("가이드 없음"));
    }
}
