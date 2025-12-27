package org.example.tpo.common.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.tpo.common.exception.model.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MypageErrorCode implements BaseErrorCode {

    // 프로필 수정
    PROFILE_UPDATE_ERROR("M001", "프로필 수정 중 에러가 발생했습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
