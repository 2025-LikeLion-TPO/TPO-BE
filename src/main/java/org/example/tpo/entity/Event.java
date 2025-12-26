package org.example.tpo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    /** 사용자 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    /** 지인 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    /** 이벤트 제목 */
    @Column(nullable = false, length = 100)
    private String eventTitle;

    /** 이벤트 상태 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventStatus eventStatus;

    /** 이벤트 타입 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EventType eventType;

    /** 이벤트 날짜 */
    @Column(nullable = false)
    private LocalDate eventDate;

    /** 메모 */
    @Column(columnDefinition = "TEXT")
    private String eventMemo;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /* ===============================
     * ENUM 정의
     * =============================== */

    public enum EventStatus {
        SCHEDULED,   // 예정
        COMPLETED,   // 완료
        CANCELED     // 취소
    }

    public enum EventType {
        BIRTHDAY,
        PROMOTION,
        WEDDING,
        ETC
    }
}

