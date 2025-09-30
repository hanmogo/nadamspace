package umc.nadamspace.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import umc.nadamspace.domain.enums.Gender;

public class UserRequestDTO {


    //회원가입
    @Getter
    @AllArgsConstructor
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
    @AllArgsConstructor
    public static class LoginDTO {
        private String email;
        private String password;
    }

    @Getter
    public static class ChangePasswordDTO {
        private String currentPassword;
        private String newPassword;
    }

    @Getter
    public static class UpdateProfileDTO {
        private String name;
        private String email;
    }
}