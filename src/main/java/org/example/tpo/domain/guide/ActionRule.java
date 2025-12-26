package org.example.tpo.domain.guide;

import org.example.tpo.entity.Event;
import java.util.List;

public class ActionRule {

    public static List<String> generate(Event event) {

        return switch (event.getEventType().getName()) {

            case "WEDDING" ->
                    List.of("축의금 준비", "예식 일정 확인", "당일 참석");

            case "VISIT" ->
                    List.of("방문 가능 여부 확인", "간단한 선물 준비", "짧게 방문");

            case "HOUSEWARMING" ->
                    List.of("필요한 물품 확인", "방문 일정 조율", "선물 전달");

            default ->
                    List.of("선물 준비", "메시지 작성", "당일 전달");
        };
    }
}
