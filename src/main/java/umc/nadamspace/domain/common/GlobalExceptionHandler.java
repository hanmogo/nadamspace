package umc.nadamspace.domain.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import umc.nadamspace.domain.enums.ErrorCode;
import umc.nadamspace.dto.ApiResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // 우리가 직접 정의한 GeneralException이 발생했을 때 처리
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(GeneralException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null));
    }

    // IllegalArgumentException 예외가 발생했을 때 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(ErrorCode._BAD_REQUEST.getHttpStatus())
                .body(ApiResponse.onFailure(ErrorCode._BAD_REQUEST.getCode(), e.getMessage(), null));
    }

    // SecurityException 예외가 발생했을 때 처리 (권한 없을 때)
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiResponse<?>> handleSecurityException(SecurityException e) {
        return ResponseEntity
                .status(ErrorCode._FORBIDDEN.getHttpStatus())
                .body(ApiResponse.onFailure(ErrorCode._FORBIDDEN.getCode(), e.getMessage(), null));
    }

    // 기타 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        return ResponseEntity
                .status(ErrorCode._INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(ApiResponse.onFailure(ErrorCode._INTERNAL_SERVER_ERROR.getCode(), e.getMessage(), null));
    }
}