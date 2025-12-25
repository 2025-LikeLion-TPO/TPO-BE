package org.example.tpo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tpo.common.response.BaseResponse;
import org.example.tpo.dto.user.request.LoginRequestDto;
import org.example.tpo.dto.user.request.SignUpRequestDto;
import org.example.tpo.dto.user.response.LoginResponseDto;
import org.example.tpo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "유저 관련 API")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(
            summary = "회원가입",
            description = """
          개발 테스트용 회원가입 API 입니다.
          """
    )
    public BaseResponse<SignUpRequestDto> signup(SignUpRequestDto signUpRequestDto) {
        userService.signUp(signUpRequestDto);

        return BaseResponse.success(signUpRequestDto);
    }

    @PostMapping("/login")
    @Operation(
            summary = "로그인",
            description = """
          `username`, `password` 로 로그인합니다.
          """
    )
    public BaseResponse<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        LoginResponseDto res = userService.login(loginRequestDto);

        return BaseResponse.success(res);
    }

}
