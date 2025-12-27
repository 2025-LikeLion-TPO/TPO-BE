package org.example.tpo.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.tpo.dto.home.response.HomeResponse;
import org.example.tpo.entity.Users;
import org.example.tpo.service.HomeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/api/home")
    @Operation(summary = "홈 조회", description = "홈 화면에 필요한 데이터(유저명, 최근 지인, 오늘 이벤트)를 반환합니다.")
    public HomeResponse getHome(@AuthenticationPrincipal Users user) {
        return homeService.getHome(user);
    }
}
