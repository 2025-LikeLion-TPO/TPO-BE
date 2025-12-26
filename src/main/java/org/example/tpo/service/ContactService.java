package org.example.tpo.service;

import jakarta.validation.Valid;
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
            @Valid ContactCreateRequest request, // <- @Valid 추가
            MultipartFile profileImage
    ) {
        if (user == null) {
            throw new IllegalArgumentException("로그인한 유저 정보가 필요합니다.");
        }

        if (request.getCommunicationFrequency() == null || request.getCommunicationFrequency().isBlank()) {
            throw new IllegalArgumentException("communicationFrequency는 필수입니다.");
        }

        Contact contact = Contact.builder()
                .user(user)
                .contactName(request.getContactName())
                .temperature(request.getTemperature())
                .relationshipType(request.getRelationshipType())
                .contactMethod(request.getContactMethod())
                .communicationFrequency(request.getCommunicationFrequency())
                .receiveCount(request.getReceiveCount() != null ? request.getReceiveCount() : 0)
                .giveCount(0)
                .relationshipMemo(request.getRelationshipMemo())
                .build();

        // 이미지 업로드
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = fileUploadService.uploadContactProfile(contact.getContactId(), profileImage);
            contact.setProfileImageUrl(imageUrl);
        }

        contactRepository.save(contact);

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