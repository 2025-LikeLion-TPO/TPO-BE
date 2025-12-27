package org.example.tpo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tpo.common.response.BaseResponse;
import org.example.tpo.dto.mypage.request.MypageProfileUpdateRequestDto;
import org.example.tpo.dto.mypage.response.MypageHistoryResponseDto;
import org.example.tpo.dto.mypage.response.MypageProfileResponseDto;
import org.example.tpo.dto.user.request.SignUpRequestDto;
import org.example.tpo.entity.Users;
import org.example.tpo.service.MypageService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Tag(name = "Mypage", description = "마이페이지 관련 API")
public class MypageController {

    private final MypageService mypageService;


    @GetMapping
    @Operation(
            summary = "마이페이지 프로필 조회",
            description = """
        로그인한 사용자의 마이페이지 프로필 정보를 조회하는 API입니다.

        반환되는 프로필 정보는 다음과 같습니다.
        - `profileImage` : S3에 저장된 사용자 프로필 이미지의 접근 URL입니다.
          프론트엔드는 해당 URL을 그대로 사용하여 이미지를 렌더링합니다.
        - `nickname` : 사용자의 닉네임입니다.
        - `age` : 사용자 나이(만 나이 기준)입니다.
        - `job` : 사용자의 직업 정보입니다.
        - `income` : 사용자의 소득 정보 또는 소득 구간 문자열입니다.
        - `preference` : 사용자의 성향 정보입니다.

        `preference` 필드는 아래 값 중 하나를 가집니다.
        - `CONSERVATIVE` : 절약형
        - `NEUTRAL` : 표준형
        - `AGGRESSIVE` : 케어형
        """
    )
    public BaseResponse<MypageProfileResponseDto> profile(@AuthenticationPrincipal Users user) {
        MypageProfileResponseDto responseDto = mypageService.profile(user);

        return BaseResponse.success("프로필을 성공적으로 조회하였습니다.", responseDto);
    }

    @PutMapping(
        value = "/profile/update",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "마이페이지 프로필 수정",
            description = """
        로그인한 사용자의 마이페이지 프로필 정보를 수정하는 API입니다.

        반환되는 프로필 정보는 다음과 같습니다.
        - `profileImage` : MultipleFile형식. 이미지 업로드.
        - `nickname` : 사용자의 닉네임입니다.
        - `age` : 사용자 나이(만 나이 기준)입니다.
        - `job` : 사용자의 직업 정보입니다.
        - `income` : 사용자의 소득 정보 또는 소득 구간 문자열입니다.
        - `preference` : 사용자의 성향 정보입니다.

        `preference` 필드는 아래 값 중 하나를 가집니다.
        - `CONSERVATIVE` : 절약형
        - `NEUTRAL` : 표준형
        - `AGGRESSIVE` : 케어형
        """
    )
    public BaseResponse<MypageProfileResponseDto> profileUpdate(@AuthenticationPrincipal Users user, @ModelAttribute MypageProfileUpdateRequestDto requestDto) {
        MypageProfileResponseDto responseDto = mypageService.profileUpdate(user, requestDto);

        return BaseResponse.success("프로필을 성공적으로 수정하였습니다.", responseDto);
    }

    @GetMapping("/history/{period}")
    @Operation(
            summary = "마이페이지 히스토리 조회",
            description = """
        로그인한 사용자의 선물 히스토리를 조회하는 API입니다.

        `period` 에는 다음과 같은 문자열이 들어갑니다.
        - '1MONTH' : 최근 1달 동안의 기록을 조회합니다.
        - '3MONTH' : 최근 3달 동안의 기록을 조회합니다.
        - '1YEAR' : 최근 1년동안의 기록을 조회합니다.
        - 'ALL' : 모든 기록을 조회합니다.
        """
    )
    public BaseResponse<List<MypageHistoryResponseDto>> history(@AuthenticationPrincipal Users user,
                                                                @PathVariable String period) {
        List<MypageHistoryResponseDto> responseDto = mypageService.history(user, period);

        return BaseResponse.success("기록을 성공적으로 조회하였습니다.", responseDto);
    }

}
