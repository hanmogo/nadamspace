package umc.nadamspace.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import umc.nadamspace.domain.enums.ErrorCode;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException{

    private final ErrorCode errorCode;

//    public GeneralException(ErrorCode errorCode) {
//        super(errorCode.getMessage());
//        this.errorCode = errorCode;
//    }
//
//    public GeneralException(ErrorCode errorCode, String message) {
//        super(message);
//        this.errorCode = errorCode;
//    }
}
