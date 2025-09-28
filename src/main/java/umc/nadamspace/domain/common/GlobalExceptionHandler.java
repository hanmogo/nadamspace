package umc.nadamspace.domain.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import umc.nadamspace.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 우리가 직접 정의한 예외가 발생했을 때 처리
    /*
    @ExceptionHandler(GeneralException.class)
    public ApiResponse<Object> handleGeneralException(GeneralException e) {
        return ApiResponse.onFailure(e.getCode(), e.getMessage(), null);
    }
    */

    // IllegalArgumentException 예외가 발생했을 때 처리
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // HTTP 상태 코드를 400으로 설정
    public ApiResponse<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ApiResponse.onFailure("COMMON400", e.getMessage(), null);
    }

    // SecurityException 예외가 발생했을 때 처리 (권한 없을 때)
    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // HTTP 상태 코드를 403으로 설정
    public ApiResponse<String> handleSecurityException(SecurityException e) {
        return ApiResponse.onFailure("AUTH403", e.getMessage(), null);
    }

    // 기타 모든 예외 처리
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // HTTP 상태 코드를 500으로 설정
    public ApiResponse<String> handleException(Exception e) {
        return ApiResponse.onFailure("SERVER500", "서버 에러가 발생했습니다.", null);
    }
}