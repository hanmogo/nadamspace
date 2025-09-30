package umc.nadamspace.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //================================================================
    // 공통(Common) 에러 코드
    //================================================================
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH403", "금지된 요청입니다."),


    //================================================================
    // 유저(User) 관련 에러 코드
    //================================================================
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4041", "해당 사용자를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER4001", "이미 사용 중인 이메일입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "USER4002", "비밀번호가 일치하지 않습니다."),


    //================================================================
    // 일기(Diary) 관련 에러 코드
    //================================================================
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "DIARY4041", "해당 일기를 찾을 수 없습니다."),
    DIARY_UNAUTHORIZED(HttpStatus.FORBIDDEN, "DIARY4031", "해당 일기에 대한 수정/삭제 권한이 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}