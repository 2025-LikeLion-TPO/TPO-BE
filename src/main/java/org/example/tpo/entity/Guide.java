package org.example.tpo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "guides")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guideId;

    /** 이벤트 타입 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Event.EventType eventType;

    /** 관계 유형 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RelationshipType relationshipType;

    /** 최소 금액 */
    @Column(nullable = false)
    private Integer minPrice;

    /** 최대 금액 */
    @Column(nullable = false)
    private Integer maxPrice;

    /** 메시지 템플릿 */
    @Column(columnDefinition = "TEXT")
    private String messageTemplate;

    /** 행동 가이드 */
    @Column(columnDefinition = "TEXT")
    private String actionGuide;

    /** 팁 문구 */
    @Column(columnDefinition = "TEXT")
    private String tipText;

    /* ===============================
     * ENUM
     * =============================== */
    public enum RelationshipType {
        FRIEND,
        FAMILY,
        COLLEAGUE,
        ACQUAINTANCE,
        ETC
    }
}

