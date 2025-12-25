package org.example.tpo.entity;

import lombok.*;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname; // 또는 nickname

    @Column
    private Integer age;

    @Column(length = 50)
    private String job;

    @Column
    private Long income; // 연 소득 기준 (만원/원 단위는 정책에 맞게)

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserPreference preference; // 성향 ENUM

//    @Column(nullable = false)
//    private Boolean onboardingCompleted = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum UserPreference {
        CONSERVATIVE,   // 보수적
        NEUTRAL,        // 중립
        AGGRESSIVE      // 공격적
    }
}

