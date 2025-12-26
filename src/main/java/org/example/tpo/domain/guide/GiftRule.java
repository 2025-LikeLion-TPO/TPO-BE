package org.example.tpo.domain.guide;

import org.example.tpo.entity.Event;
import java.util.List;

public class GiftRule {

    public static List<String> recommend(Event event) {
        return switch (event.getEventType().getName()) {

            case "BIRTHDAY" ->
                    List.of("기프티콘", "케이크", "문구");

            case "PROMOTION" ->
                    List.of("꽃", "기프티콘");

            case "JOIN" ->
                    List.of("문구", "기프티콘");

            case "LEAVE" ->
                    List.of("기프티콘", "감사카드");

            case "WEDDING" ->
                    List.of("축의금", "생활용품");

            case "BIRTH" ->
                    List.of("유아용품", "기저귀", "상품권");

            case "VISIT" ->
                    List.of("과일", "건강식품", "기프티콘");

            case "HOUSEWARMING" ->
                    List.of("생활용품", "와인", "디퓨저");

            default ->
                    List.of("기프티콘");
        };
    }
}


