package umc.nadamspace.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import umc.nadamspace.domain.User;
import umc.nadamspace.dto.ApiResponse;
import umc.nadamspace.dto.UserRequestDTO;
import umc.nadamspace.dto.UserResponseDTO;
import umc.nadamspace.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /**
     * 내 정보 조회 API
     * [GET] /api/users/me
     */
    @GetMapping("/me")
    public ApiResponse<UserResponseDTO> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = Long.valueOf(userDetails.getUsername());
        User user = userService.findUserById(currentUserId);
        return ApiResponse.onSuccess(UserResponseDTO.from(user));
    }

    /**
     * 내 정보 수정 API
     * [PATCH] /api/users/me
     */
    @PatchMapping("/me")
    public ApiResponse<UserResponseDTO> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserRequestDTO.UpdateProfileDTO request) {

        Long currentUserId = Long.valueOf(userDetails.getUsername());
        User updatedUser = userService.updateProfile(currentUserId, request);
        return ApiResponse.onSuccess(UserResponseDTO.from(updatedUser));
    }

    /**
     * 비밀번호 변경 API
     * [PATCH] /api/users/me/password
     */
    @PatchMapping("/me/password")
    public ApiResponse<String> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserRequestDTO.ChangePasswordDTO request) {

        Long currentUserId = Long.valueOf(userDetails.getUsername());
        userService.changePassword(currentUserId, request);
        return ApiResponse.onSuccess("비밀번호가 성공적으로 변경되었습니다.");
    }

    /**
     * 회원 탈퇴 API
     * [DELETE] /api/users/me
     */
    @DeleteMapping("/me")
    public ApiResponse<String> withdraw(@AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = Long.valueOf(userDetails.getUsername());
        userService.withdraw(currentUserId);
        return ApiResponse.onSuccess("회원 탈퇴가 성공적으로 처리되었습니다.");
    }
}