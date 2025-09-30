package umc.nadamspace.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.nadamspace.domain.User;
import umc.nadamspace.dto.ApiResponse;
import umc.nadamspace.dto.UserResponseDTO;
import umc.nadamspace.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserResponseDTO> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = Long.valueOf(userDetails.getUsername());
        User user = userService.findUserById(currentUserId); // Service에서 User 엔티티 조회

        // Entity를 DTO로 변환하여 반환
        return ApiResponse.onSuccess(UserResponseDTO.from(user));
    }
}