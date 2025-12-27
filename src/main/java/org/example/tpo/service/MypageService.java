package org.example.tpo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tpo.common.exception.CustomException;
import org.example.tpo.common.exception.errorCode.AuthErrorCode;
import org.example.tpo.common.exception.errorCode.GlobalErrorCode;
import org.example.tpo.common.exception.errorCode.MypageErrorCode;
import org.example.tpo.config.AppProperties;
import org.example.tpo.dto.mypage.request.MypageProfileUpdateRequestDto;
import org.example.tpo.dto.mypage.response.MypageHistoryResponseDto;
import org.example.tpo.dto.mypage.response.MypageProfileResponseDto;
import org.example.tpo.entity.Contact;
import org.example.tpo.entity.Event;
import org.example.tpo.entity.Users;
import org.example.tpo.repository.EventRepository;
import org.example.tpo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageService {

    @Value("${app.s3.bucket}")
    private String bucket;
    @Value("${app.s3.root-prefix}")
    private String prefix;


    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final S3Client s3Client;
    private final AppProperties appProperties;

    public MypageProfileResponseDto profile(Users user) {

        String profileImage = user.getProfileImage();

        if(user.getProfileImage() == null){
            profileImage = String.format(
                    "https://%s.s3.ap-northeast-2.amazonaws.com/%s/default.jpeg",
                    bucket,
                    prefix
            );

        }

        MypageProfileResponseDto mypageProfileResponseDto = MypageProfileResponseDto.builder()
                .profileImage(profileImage)
                .nickname(user.getNickname())
                .age(user.getAge())
                .job(user.getJob())
                .income(user.getIncome())
                .preference(user.getPreference())
                .build();

        return mypageProfileResponseDto;
    }

    public MypageProfileResponseDto profileUpdate(Users user, MypageProfileUpdateRequestDto requestDto) {

        String newProfileImage;

        try {

            String originProfileImage = user.getProfileImage();

            newProfileImage = uploadProfileImage(requestDto.getProfileImage());

            user.setProfileImage(newProfileImage);
            user.setJob(requestDto.getJob());
            user.setIncome(requestDto.getIncome());
            user.setPreference(requestDto.getPreference());
            user.setNickname(requestDto.getNickname());
            user.setAge(requestDto.getAge());

            userRepository.save(user);

            //기존 이미지 S3에서 삭제
            if (originProfileImage != null) {
                deleteImageIfExists(originProfileImage);
            }

        }catch (Exception e) {
            log.error("프로필 수정 중 에러 발생: userId={}, error={}", user.getId(), e.getMessage(), e);
            throw new CustomException(MypageErrorCode.PROFILE_UPDATE_ERROR);
        }

        MypageProfileResponseDto responseDto = MypageProfileResponseDto.builder()
                .profileImage(user.getProfileImage())
                .nickname(user.getNickname())
                .age(user.getAge())
                .job(user.getJob())
                .income(user.getIncome())
                .preference(user.getPreference())
                .build();

        return responseDto;
    }

    public List<MypageHistoryResponseDto> history(Users user, String period) {

        List<Event> events;

        if (period.equals("ALL")) {

            events = eventRepository
                    .findByUserAndEventStatusOrderByEventDateDesc(
                            user,
                            Event.EventStatus.DONE
                    );

        } else {

            LocalDate now = LocalDate.now();
            LocalDate endPeriod;

            if (period.equals("1MONTH")) {
                endPeriod = now.minusMonths(1);
            } else if (period.equals("3MONTH")) {
                endPeriod = now.minusMonths(3);
            } else if (period.equals("1YEAR")) {
                endPeriod = now.minusMonths(12);
            } else {
                throw new IllegalArgumentException("Invalid period");
            }

            events = eventRepository
                    .findByUserAndEventStatusAndEventDateBetweenOrderByEventDateDesc(
                            user,
                            Event.EventStatus.DONE,
                            endPeriod,
                            now
                    );

        }

        String defaultProfileImageUrl = String.format(
                "https://%s.s3.ap-northeast-2.amazonaws.com/%s/default.jpeg",
                bucket,
                prefix
        );

        List<MypageHistoryResponseDto> histories = events.stream()
                .map(event -> {

                    Contact contact = event.getContact(); // 또는 event.getTarget()

                    return MypageHistoryResponseDto.builder()
                            // 날짜
                            .date(event.getEventDate())

                            // 카테고리
                            .eventType(event.getEventType().getName())
                            // 예: BIRTHDAY → "생일"

                            // 카드 제목
                            .title(event.getEventTitle())

                            // 대상 정보
                            .name(contact.getContactName())
                            .relationship(contact.getRelationshipType())
                            .temperature(contact.getTemperature())

                            // 프로필 이미지
                            .profileImageUrl(
                                    contact.getProfileImageUrl() != null
                                            ? contact.getProfileImageUrl()
                                            : defaultProfileImageUrl
                            )

                            .build();
                })
                .toList();

        return histories;
    }


    //이미지 삭제
    private String extractS3Key(String imageUrl) {
        if (imageUrl == null) return null;

        // bucket 도메인 이후 경로만 추출
        return imageUrl.substring(imageUrl.indexOf(".amazonaws.com/") + 15);
    }

    private void deleteImageIfExists(String imageUrl) {

        if (imageUrl == null) {
            return; // 삭제할 이미지 없음
        }

        String key = extractS3Key(imageUrl);

        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(deleteRequest);
    }

    //이미지 추가
    /**
     * 프로필 이미지 업로드
     */
    public String uploadProfileImage(MultipartFile file) {

        // 1. 파일 검증
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        // 2. S3 key 생성
        String key = generateProfileImageKey(file.getOriginalFilename());

        // 3. 업로드
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(appProperties.getS3().getBucket())
                .key(key)
                .contentType(file.getContentType())
//                .acl(ObjectCannedACL.PUBLIC_READ) // URL 접근 허용
                .build();

        try {
            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(
                            file.getInputStream(),
                            file.getSize()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }

        // 4. 접근 URL 반환
        return buildFileUrl(key);
    }

    /**
     * S3 접근 URL 생성
     */
    private String buildFileUrl(String key) {

        String cloudFront = appProperties.getS3().getCloudfrontDomain();

        if (cloudFront != null && !cloudFront.isBlank()) {
            return cloudFront + "/" + key;
        }

        return "https://%s.s3.%s.amazonaws.com/%s"
                .formatted(
                        appProperties.getS3().getBucket(),
                        appProperties.getS3().getRegion(),
                        key
                );
    }

    /**
     * S3 key 생성 (중복 방지)
     */
    private String generateProfileImageKey(String originalFilename) {

        String extension = originalFilename.substring(
                originalFilename.lastIndexOf(".")
        );

        return "%s/%s%s"
                .formatted(
                        appProperties.getS3().getRootPrefix(), // images
                        UUID.randomUUID(),
                        extension
                );
    }
}


