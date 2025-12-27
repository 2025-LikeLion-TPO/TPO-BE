package org.example.tpo.domain.guide;

import org.example.tpo.entity.Event;
import java.util.List;

public class WarningRule {

    public static List<String> generate(Event event) {

        return switch (event.getEventType().getName()) {

            case "VISIT" ->
                    List.of("향이 강한 선물은 피하세요.", "장시간 방문은 삼가세요.");

            case "PROMOTION", "JOIN", "LEAVE" ->
                    List.of("과도한 금액의 선물은 피하세요.");

            default ->
                    List.of("상대방의 취향을 고려하세요.");
        };
    }
}

