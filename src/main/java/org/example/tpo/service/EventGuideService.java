package org.example.tpo.service;

import lombok.RequiredArgsConstructor;
import org.example.tpo.domain.guide.ActionRule;
import org.example.tpo.domain.guide.GiftRule;
import org.example.tpo.domain.guide.MessageRule;
import org.example.tpo.domain.guide.WarningRule;
import org.example.tpo.dto.event.response.EventGuideResponse;
import org.example.tpo.entity.Event;
import org.example.tpo.entity.Users;
import org.example.tpo.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.tpo.domain.guide.GiftRule;
import org.example.tpo.domain.guide.MessageRule;
import org.example.tpo.domain.guide.ActionRule;
import org.example.tpo.domain.guide.WarningRule;


import java.util.List;

@Service
@RequiredArgsConstructor
public class EventGuideService {

    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public EventGuideResponse generateGuide(Users user, Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트 없음"));

        if (!user.getId().equals(event.getUser().getId())) {
            throw new IllegalArgumentException("권한 없음");
        }

        // =========================
        // 1️⃣ 친밀도 점수 계산 (임시)
        // =========================
        // ⚠️ 실제로는 관계유형/교류횟수/최근성 기반
        int intimacyScore = calculateIntimacy(event);

        // =========================
        // 2️⃣ 밴드 & 배수
        // =========================
        Band band = Band.fromScore(intimacyScore);

        // =========================
        // 3️⃣ 기본 금액대 (B2 기준)
        AmountRange baseRange = getBaseAmount(event);

        int minAmount = roundTo5000((int) (baseRange.min * band.multiplier));
        int maxAmount = roundTo5000((int) (baseRange.max * band.multiplier));

        // =========================
        // 4️⃣ 추천 카테고리
        // =========================
        List<String> giftCategories = GiftRule.recommend(event);

        // =========================
        // 5️⃣ 메시지 카드
        // =========================
        List<String> messageCards = MessageRule.generate(event);

        // =========================
        // 6️⃣ 행동 가이드
        // =========================
        List<String> actionGuides = ActionRule.generate(event);

        // =========================
        // 7️⃣ 주의사항
        // =========================
        List<String> warnings = WarningRule.generate(event);

        return EventGuideResponse.builder()
                .intimacyScore(intimacyScore)
                .intimacyBand(band.name())
                .amountMultiplier(band.multiplier)
                .minAmount(minAmount)
                .maxAmount(maxAmount)
                .giftCategories(giftCategories)
                .messageCards(messageCards)
                .actionGuides(actionGuides)
                .warnings(warnings)
                .build();
    }

    private int calculateIntimacy(Event event) {
        // TODO: 관계유형 + 교류빈도 + 최근성
        // 지금은 임시
        return 55;
    }

    private AmountRange getBaseAmount(Event event) {
        String type = event.getEventType().getName();

        return switch (type) {
            case "BIRTHDAY" -> new AmountRange(30000, 50000);
            case "PROMOTION" -> new AmountRange(30000, 70000);
            case "JOIN" -> new AmountRange(20000, 50000);
            case "LEAVE" -> new AmountRange(20000, 50000);
            case "WEDDING" -> new AmountRange(50000, 100000);
            case "BIRTH" -> new AmountRange(50000, 100000);
            case "VISIT" -> new AmountRange(30000, 70000);
            case "HOUSEWARMING" -> new AmountRange(30000, 80000);
            default -> new AmountRange(20000, 50000);
        };
    }

    private int roundTo5000(int amount) {
        return Math.round(amount / 5000f) * 5000;
    }

    // ================= ENUM / RECORD =================

    enum Band {
        B0(0, 19, 0.6),
        B1(20, 39, 0.8),
        B2(40, 59, 1.0),
        B3(60, 79, 1.2),
        B4(80, 100, 1.5);

        final int min;
        final int max;
        final double multiplier;

        Band(int min, int max, double multiplier) {
            this.min = min;
            this.max = max;
            this.multiplier = multiplier;
        }

        static Band fromScore(int score) {
            for (Band b : values()) {
                if (score >= b.min && score <= b.max) return b;
            }
            return B2;
        }
    }

    record AmountRange(int min, int max) {}
}
