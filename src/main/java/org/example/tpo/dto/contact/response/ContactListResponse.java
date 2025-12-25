package org.example.tpo.dto.contact.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContactListResponse {

    private Long contactId;
    private String contactName;
    private Integer temperature;
    private String relationshipType;
}
