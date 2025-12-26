package org.example.tpo.dto.contact.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ContactListWrapperResponse {

    private List<ContactListResponse> contacts;
}