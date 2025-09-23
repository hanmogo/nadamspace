package umc.nadamspace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenDTO {
    private String grantType; // "Bearer"
    private String accessToken;
    private String refreshToken;
}