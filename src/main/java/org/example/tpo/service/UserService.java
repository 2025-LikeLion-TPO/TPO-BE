package org.example.tpo.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tpo.common.exception.CustomException;
import org.example.tpo.common.exception.errorCode.AuthErrorCode;
import org.example.tpo.common.response.BaseResponse;
import org.example.tpo.dto.user.request.LoginRequestDto;
import org.example.tpo.dto.user.request.SignUpRequestDto;
import org.example.tpo.dto.user.response.LoginResponseDto;
import org.example.tpo.entity.Users;
import org.example.tpo.repository.UserRepository;
import org.example.tpo.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /* ===============================
     * 회원가입
     * =============================== */
    @Transactional
    public void signUp(SignUpRequestDto request) {

        // 1. 아이디 중복 체크
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(AuthErrorCode.USERNAME_ALREADY_EXISTS);
        }

        // 2. 비밀번호 확인
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new CustomException(AuthErrorCode.PASSWORD_MISMATCH);
        }

        // 3. 사용자 생성
        Users user = Users.builder()
                .username(request.getUsername())
                .nickname(request.getNickname())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        log.info("회원가입 완료: username={}, nickname={}", user.getUsername(), user.getNickname());

    }

    /* ===============================
     * 로그인
     * =============================== */
    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto request) {

        // 1. 사용자 조회
        Users user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(AuthErrorCode.INVALID_CREDENTIALS));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(AuthErrorCode.INVALID_CREDENTIALS);
        }

        // 3. JWT 생성
        String accessToken = jwtUtil.generateAccessToken(
                user.getId(),
                user.getUsername()
        );

        String refreshToken = jwtUtil.generateRefreshToken(
                user.getId()
        );

        log.info("로그인 성공: userId={}, username={}, nickname={}", user.getId(), user.getUsername(), user.getNickname());

        // 4. 응답 DTO 생성
        LoginResponseDto response = LoginResponseDto.builder()
                .username(user.getUsername())
                .age(user.getAge())
                .nickname(user.getNickname())
                .job(user.getJob())
                .income(user.getIncome())
                .preference(user.getPreference())
                .accessToken(accessToken)
                .build();

        return response;
    }
}
