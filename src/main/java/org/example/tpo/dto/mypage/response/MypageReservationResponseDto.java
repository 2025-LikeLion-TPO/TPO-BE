package org.example.tpo.dto.mypage.response;

import lombok.*;

import java.time.LocalDate;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MypageReservationResponseDto {
    private Long eventId;
    // 날짜
    private LocalDate date;          // 2025-12-30

    // 카테고리
    private String eventType;             // "생일"

    // 카드 제목
    private String title;            // "2만원대 기프트 카드 + 생일 축하..."

    // 대상 정보
    private String name;             // "강성신"
    private String relationship;      // "대학생"
    private Integer temperature;             // 30

    // 프로필 이미지
    private String profileImageUrl;  // S3 URL
}
