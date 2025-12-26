package org.example.tpo.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.tpo.common.exception.errorCode.GlobalErrorCode;
import org.example.tpo.common.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // 커스텀 예외
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<BaseResponse<Object>> handleCustomException(CustomException ex) {
    log.error("Custom 오류 발생: {}", ex.getMessage());
    return ResponseEntity
        .status(ex.getErrorCode().getStatus())
        .body(BaseResponse.failure(
            ex.getErrorCode().getCode(),       // 예: "E001"
            ex.getErrorCode().getMessage()     // 예: "임시 주소 저장 실패"
        ));
  }

  // Validation 실패
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseResponse<Object>> handleValidationException(
      MethodArgumentNotValidException ex) {
    String errorMessages =
        ex.getBindingResult().getFieldErrors().stream()
            .map(e -> String.format("[%s] %s", e.getField(), e.getDefaultMessage()))
            .collect(Collectors.joining(" / "));

    log.warn("Validation 오류 발생: {}", errorMessages);

    return ResponseEntity
        .badRequest()
        .body(BaseResponse.failure("VALIDATION_ERROR", errorMessages));
  }

  // 로그인 안 한 경우 및 IllegalArgumentException 처리
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<BaseResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
    log.warn("IllegalArgumentException 발생: {}", ex.getMessage());
    if (ex.getMessage() != null && ex.getMessage().contains("로그인한 유저")) {
      return ResponseEntity
              .status(HttpStatus.UNAUTHORIZED)
              .body(BaseResponse.failure("AUTH001", "로그인이 필요합니다."));
    }
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(BaseResponse.failure("BAD001", ex.getMessage()));
  }

  // 예상치 못한 예외
  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseResponse<Object>>  handleException(Exception ex) {
    log.error("Server 오류 발생: ", ex);

    return ResponseEntity
        .status(GlobalErrorCode.INTERNAL_SERVER_ERROR.getStatus())
        .body(BaseResponse.failure(
            GlobalErrorCode.INTERNAL_SERVER_ERROR.getCode(),
            GlobalErrorCode.INTERNAL_SERVER_ERROR.getMessage()
        ));
  }


}