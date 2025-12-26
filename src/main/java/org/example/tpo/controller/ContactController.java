package org.example.tpo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tpo.dto.contact.request.ContactCreateRequest;
import org.example.tpo.dto.contact.response.ContactCreateResponse;
import org.example.tpo.dto.contact.response.ContactDetailResponse;
import org.example.tpo.dto.contact.response.ContactListWrapperResponse;
import org.example.tpo.dto.event.response.ContactEventListWrapperResponse;
import org.example.tpo.entity.Users;
import org.example.tpo.repository.UserRepository;
import org.example.tpo.service.ContactService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.example.tpo.dto.contact.request.ContactUpdateRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;
    private final UserRepository userRepository; // 추가

    @GetMapping
    @Operation(
            summary = "지인 목록 조회",
            description = """
                    지인 목록 조회 API 입니다.
                    """
    )
    public ContactListWrapperResponse getContacts(
            @AuthenticationPrincipal Users user
    ) {
        return contactService.getContacts(user);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "지인 추가",
            description = """
                    지인 추가 POST API 입니다.
                    """
    )
    public ContactCreateResponse createContact(
            @AuthenticationPrincipal Users user,
            @Parameter(description = "연락처 이름") @RequestParam String contactName,
            @Parameter(description = "온도") @RequestParam int temperature,
            @Parameter(description = "관계") @RequestParam String relationshipType,
            @Parameter(description = "연락 수단") @RequestParam String contactMethod,
            @Parameter(description = "주기") @RequestParam String communicationFrequency,
            @Parameter(description = "받은 횟수") @RequestParam(required = false) Integer receiveCount,
            @Parameter(description = "메모") @RequestParam(required = false) String relationshipMemo,
            @Parameter(description = "프로필 이미지") @RequestPart(required = false) MultipartFile profileImage
    ) {
        ContactCreateRequest request = new ContactCreateRequest(
                contactName, temperature, relationshipType, contactMethod,
                communicationFrequency, receiveCount, relationshipMemo
        );
        return contactService.createContact(user, request, profileImage);
    }

    @PutMapping(value = "/{contactId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "지인 수정", description = "지인 정보 수정 PUT API 입니다.")
    public ContactCreateResponse updateContact(
            @AuthenticationPrincipal Users user,
            @PathVariable Long contactId,
            @Parameter(description = "연락처 이름") @RequestParam String contactName,
            @Parameter(description = "온도") @RequestParam int temperature,
            @Parameter(description = "관계") @RequestParam String relationshipType,
            @Parameter(description = "연락 수단") @RequestParam String contactMethod,
            @Parameter(description = "주기") @RequestParam String communicationFrequency,
            @Parameter(description = "받은 횟수") @RequestParam(required = false) Integer receiveCount,
            @Parameter(description = "메모") @RequestParam(required = false) String relationshipMemo,
            @Parameter(description = "프로필 이미지") @RequestPart(required = false) MultipartFile profileImage
    ) {
        ContactUpdateRequest request = new ContactUpdateRequest(
                contactName, temperature, relationshipType, contactMethod,
                communicationFrequency,
                (receiveCount != null ? receiveCount : 0),
                relationshipMemo
        );

        return contactService.updateContact(user, contactId, request, profileImage);
    }

    @DeleteMapping("/{contactId}")
    @Operation(summary = "지인 삭제", description = "지인 삭제 DELETE API 입니다.")
    public void deleteContact(
            @AuthenticationPrincipal Users user,
            @PathVariable Long contactId
    ) {
        contactService.deleteContact(user, contactId);
    }

    @GetMapping("/{contactId}")
    @Operation(summary = "지인 상세 조회", description = "지인 1명 상세 정보를 조회합니다.")
    public ContactDetailResponse getContactDetail(
            @AuthenticationPrincipal Users user,
            @PathVariable Long contactId
    ) {
        return contactService.getContactDetail(user, contactId);
    }


    @GetMapping("/{contactId}/events")
    @Operation(summary = "지인과 지난 이벤트 전체보기", description = "지인 1명 지난 이벤트를 조회합니다.")
    public ContactEventListWrapperResponse getContactEvents(
            @AuthenticationPrincipal Users user,
            @PathVariable Long contactId
    ) {
        return contactService.getContactEvents(user, contactId);
    }

}






