package org.example.tpo.dto.event.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EventGuideResponse {

    // 1. 친밀도
    private int intimacyScore;        // 0~100
    private String intimacyBand;      // B0~B4
    private double amountMultiplier;  // M

    // 2. 금액
    private int minAmount;
    private int maxAmount;

    // 3. 선물 추천
    private List<String> giftCategories; // 1~3개

    // 4. 메시지 카드
    private List<String> messageCards;   // 2~3개

    // 5. 행동 가이드
    private List<String> actionGuides;   // 3~4개

    // 6. 주의사항
    private List<String> warnings;
}
