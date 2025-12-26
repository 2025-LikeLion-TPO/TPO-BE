package org.example.tpo.dto.contact.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ContactDetailResponse {
    private Long contactId;
    private String contactName;
    private Integer temperature;
    private String relationshipType;
    private String contactMethod;
    private String communicationFrequency;
    private Integer giveCount;
    private Integer receiveCount;
    private String relationshipMemo;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

