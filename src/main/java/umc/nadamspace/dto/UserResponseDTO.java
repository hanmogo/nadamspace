package umc.nadamspace.dto;

import lombok.Builder;
import lombok.Getter;
import umc.nadamspace.domain.User;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phoneNum;
    private String birthdate;
    private LocalDateTime createdAt;

    // User 엔티티를 UserResponseDTO로 변환하는 정적 팩토리 메서드
    public static UserResponseDTO from(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNum(user.getPhoneNum())
                .birthdate(String.valueOf(user.getBirthdate()))
                .createdAt(user.getCreatedAt())
                .build();
    }
}