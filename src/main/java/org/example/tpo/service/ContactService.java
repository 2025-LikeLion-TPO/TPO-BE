package org.example.tpo.service;

import lombok.RequiredArgsConstructor;
import org.example.tpo.dto.contact.request.ContactCreateRequest;
import org.example.tpo.dto.contact.response.ContactCreateResponse;
import org.example.tpo.dto.contact.response.ContactListResponse;
import org.example.tpo.dto.contact.response.ContactListWrapperResponse;
import org.example.tpo.entity.Contact;
import org.example.tpo.entity.Users;
import org.example.tpo.repository.ContactRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final FileUploadService fileUploadService;

    public ContactListWrapperResponse getContacts(Users user) {

        List<ContactListResponse> contacts = contactRepository.findByUser(user)
                .stream()
                .map(contact -> new ContactListResponse(
                        contact.getContactId(),
                        contact.getContactName(),
                        contact.getTemperature(),
                        contact.getRelationshipType()
                ))
                .toList();

        return new ContactListWrapperResponse(contacts);
    }

    public ContactCreateResponse createContact(
            Users user,
            ContactCreateRequest request,
            MultipartFile profileImage
    ) {
        Contact contact = Contact.builder()
                .user(user)
                .contactName(request.getContactName())
                .temperature(request.getTemperature())
                .relationshipType(request.getRelationshipType())
                .contactMethod(request.getContactMethod())
                .communicationFrequency(request.getCommunicationFrequency())
                .receiveCount(request.getReceiveCount())
                .giveCount(0)
                .relationshipMemo(request.getRelationshipMemo())
                .build();

        contactRepository.save(contact);

        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = fileUploadService.uploadContactProfile(
                    contact.getContactId(), profileImage
            );
            contact.setProfileImageUrl(imageUrl);
        }

        return new ContactCreateResponse(
                contact.getContactId(),
                contact.getContactName(),
                contact.getTemperature(),
                contact.getRelationshipType(),
                contact.getProfileImageUrl(),
                contact.getCreatedAt(),
                contact.getUpdatedAt()
        );
    }
}
