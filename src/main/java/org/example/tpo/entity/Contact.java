package org.example.tpo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long contactId;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;


    @Column(nullable = false, length = 50)
    private String contactName;

    @Column(nullable = false)
    private Integer temperature;

    @Column(nullable = false, length = 30)
    private String relationshipType;

    @Column(nullable = false, length = 30)
    private String contactMethod;

    @Column(nullable = false, length = 30)
    private String communicationFrequency;

    @Column(nullable = false)
    private Integer giveCount;

    @Column(nullable = false)
    private Integer receiveCount;

    @Column(length = 1000)
    private String relationshipMemo;

    @Column(length = 500)
    private String profileImageUrl;


    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
