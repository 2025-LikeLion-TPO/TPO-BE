package org.example.tpo.dto.contact.request;

import lombok.Getter;

@Getter
public class ContactCreateRequest {

    private String contactName;
    private Integer temperature;
    private String relationshipType;
    private String contactMethod;
    private String communicationFrequency;
    private Integer receiveCount;
    private String relationshipMemo;
}
