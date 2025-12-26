package org.example.tpo.domain.guide;

import org.example.tpo.entity.Event;
import java.util.List;

public class MessageRule {

    public static List<String> generate(Event event) {

        return switch (event.getEventType().getName()) {

            case "BIRTHDAY" ->
                    List.of(
                            "생일 축하해!",
                            "올해도 좋은 일만 가득하길 바라.",
                            "항상 응원할게 🎉"
                    );

            case "PROMOTION" ->
                    List.of(
                            "승진 진심으로 축하드립니다.",
                            "그동안의 노력이 빛을 봤네요."
                    );

            case "JOIN" ->
                    List.of(
                            "새 출발을 축하합니다.",
                            "앞으로의 길을 응원할게요."
                    );

            case "LEAVE" ->
                    List.of(
                            "그동안 고생 많으셨습니다.",
                            "앞날에 좋은 일만 있길 바랍니다."
                    );

            case "WEDDING" ->
                    List.of(
                            "결혼 진심으로 축하해!",
                            "행복한 날들만 가득하길 바라."
                    );

            case "BIRTH" ->
                    List.of(
                            "출산 정말 축하드립니다!",
                            "산모와 아기 모두 건강하길 바랍니다."
                    );

            case "VISIT" ->
                    List.of(
                            "빠른 쾌유를 바랍니다.",
                            "무리하지 말고 푹 쉬세요."
                    );

            case "HOUSEWARMING" ->
                    List.of(
                            "집들이 축하해!",
                            "새 집에서 좋은 추억 많이 만들길 바라."
                    );

            default ->
                    List.of("축하드립니다!");
        };
    }
}

