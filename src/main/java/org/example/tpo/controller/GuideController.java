package org.example.tpo.controller;

import lombok.RequiredArgsConstructor;
import org.example.tpo.common.response.BaseResponse;
import org.example.tpo.entity.Guide;
import org.example.tpo.service.GuideService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guides")
public class GuideController {

    private final GuideService guideService;

    @GetMapping
    public BaseResponse<Guide> getGuide(
            @RequestParam Guide.EventType eventType,
            @RequestParam Guide.RelationshipType relationshipType
    ) {
        return BaseResponse.success(
                guideService.getGuide(eventType, relationshipType)
        );
    }
}
