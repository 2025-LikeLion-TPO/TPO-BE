package org.example.tpo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "event_type")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EventType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user; // null이면 기본 타입

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private Boolean isDefault;
}

