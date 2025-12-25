package org.example.tpo.controller;

import lombok.RequiredArgsConstructor;
import org.example.tpo.dto.contact.request.ContactCreateRequest;
import org.example.tpo.dto.contact.response.ContactCreateResponse;
import org.example.tpo.dto.contact.response.ContactListWrapperResponse;
import org.example.tpo.entity.Users;
import org.example.tpo.service.ContactService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public ContactListWrapperResponse getContacts(
            @AuthenticationPrincipal Users user
    ) {
        return contactService.getContacts(user);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ContactCreateResponse createContact(
            @AuthenticationPrincipal Users user,
            @RequestPart("data") ContactCreateRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        return contactService.createContact(user, request, profileImage);
    }
}
