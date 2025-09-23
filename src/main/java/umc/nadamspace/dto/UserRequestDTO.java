package umc.nadamspace.dto;

import lombok.Getter;
import umc.nadamspace.domain.enums.Gender;

public class UserRequestDTO {


    //회원가입
    @Getter
    public static class SignUpDTO {
        private String email;
        private String password;
        private String name;
        private String phoneNumber;
        private String birthdate;
        private Gender gender;
    }

    //로그인
    @Getter
    public static class LoginDTO {
        private String email;
        private String password;
    }
}