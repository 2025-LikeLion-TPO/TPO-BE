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
import org.example.tpo.dto.contact.request.ContactUpdateRequest;
import org.springframework.transaction.annotation.Transactional;
import org.example.tpo.dto.contact.response.ContactDetailResponse;
import org.example.tpo.dto.event.response.ContactEventListWrapperResponse;
import org.example.tpo.dto.event.response.EventListResponse;
import org.example.tpo.entity.Event;
import org.example.tpo.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final FileUploadService fileUploadService;
    private final EventRepository eventRepository;

    public ContactListWrapperResponse getContacts(Users user) {

        List<ContactListResponse> contacts = contactRepository.findByUser(user)
                .stream()
                .map(contact -> new ContactListResponse(
                        contact.getContactId(),
                        contact.getContactName(),
                        contact.getTemperature(),
                        contact.getRelationshipType(),
                        contact.getProfileImageUrl()
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

        contact = contactRepository.save(contact); // 먼저 저장해서 ID 확보

        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = fileUploadService.uploadContactProfile(contact.getContactId(), profileImage);
            contact.setProfileImageUrl(imageUrl);
            contact = contactRepository.save(contact); // url 반영 저장
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

    @Transactional
    public ContactCreateResponse updateContact(
            Users user,
            Long contactId,
            @Valid ContactUpdateRequest request,
            MultipartFile profileImage
    ) {
        if (user == null) {
            throw new IllegalArgumentException("로그인한 유저 정보가 필요합니다.");
        }

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("해당 지인을 찾을 수 없습니다. contactId=" + contactId));

        // ✅ 본인 지인만 수정 가능
        if (!contact.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 지인만 수정할 수 있습니다.");
        }

        // 필드 업데이트
        contact.setContactName(request.getContactName());
        contact.setTemperature(request.getTemperature());
        contact.setRelationshipType(request.getRelationshipType());
        contact.setContactMethod(request.getContactMethod());
        contact.setCommunicationFrequency(request.getCommunicationFrequency());
        contact.setReceiveCount(request.getReceiveCount() != null ? request.getReceiveCount() : 0);
        contact.setRelationshipMemo(request.getRelationshipMemo());

        // 이미지 업로드(선택)
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = fileUploadService.uploadContactProfile(contact.getContactId(), profileImage);
            contact.setProfileImageUrl(imageUrl);
        }

        Contact saved = contactRepository.save(contact);

        return new ContactCreateResponse(
                saved.getContactId(),
                saved.getContactName(),
                saved.getTemperature(),
                saved.getRelationshipType(),
                saved.getProfileImageUrl(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteContact(Users user, Long contactId) {
        if (user == null) {
            throw new IllegalArgumentException("로그인한 유저 정보가 필요합니다.");
        }

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("해당 지인을 찾을 수 없습니다. contactId=" + contactId));

        // ✅ 본인 지인만 삭제 가능
        if (!contact.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 지인만 삭제할 수 있습니다.");
        }

        // (선택) 이미지도 같이 지우고 싶으면 fileUploadService에 delete가 있을 때 여기서 호출
        // fileUploadService.deleteContactProfile(contact.getContactId());

        contactRepository.delete(contact);
    }

    public ContactDetailResponse getContactDetail(Users user, Long contactId) {
        if (user == null) {
            throw new IllegalArgumentException("로그인한 유저 정보가 필요합니다.");
        }

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("해당 지인을 찾을 수 없습니다. contactId=" + contactId));

        // ✅ 본인 지인만 조회 가능
        if (!user.getId().equals(contact.getUser().getId())) {
            throw new IllegalArgumentException("본인의 지인만 조회할 수 있습니다.");
        }

        return new ContactDetailResponse(
                contact.getContactId(),
                contact.getContactName(),
                contact.getTemperature(),
                contact.getRelationshipType(),
                contact.getContactMethod(),
                contact.getCommunicationFrequency(),
                contact.getGiveCount(),
                contact.getReceiveCount(),
                contact.getRelationshipMemo(),
                contact.getProfileImageUrl(),
                contact.getCreatedAt(),
                contact.getUpdatedAt()
        );
    }

    public ContactEventListWrapperResponse getContactEvents(Users user, Long contactId) {
        if (user == null) throw new IllegalArgumentException("로그인한 유저 정보가 필요합니다.");

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("해당 지인을 찾을 수 없습니다."));

        // 본인 지인 검증
        if (!user.getId().equals(contact.getUser().getId())) {
            throw new IllegalArgumentException("본인의 지인만 조회할 수 있습니다.");
        }

        List<EventListResponse> events = eventRepository
                .findByContactOrderByEventDateDesc(contact)
                .stream()
                .map(event -> new EventListResponse(
                        event.getEventId(),
                        event.getEventTitle(),
                        event.getEventType().getName(),      // ✅ EventType 엔티티
                        event.getEventDate(),
                        event.getEventMemo(),
                        event.getEventStatus().name()        // ✅ enum
                ))
                .toList();

        return new ContactEventListWrapperResponse(events);
    }
}
