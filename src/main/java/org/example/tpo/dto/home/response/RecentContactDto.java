package org.example.tpo.dto.home.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecentContactDto {
    private Long contactId;
    private String contactName;
    private Integer temperature;
    private String relationshipType;
    private Integer giveCount;
    private Integer receiveCount;
}
