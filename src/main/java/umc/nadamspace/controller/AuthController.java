package umc.nadamspace.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.nadamspace.dto.ApiResponse;
import umc.nadamspace.dto.TokenDTO;
import umc.nadamspace.dto.UserRequestDTO;
import umc.nadamspace.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<Long> signUp(@RequestBody UserRequestDTO.SignUpDTO request) {
        Long newUserId = authService.signUp(request);
        return ApiResponse.onSuccess(newUserId);
    }

    @PostMapping("/login")
    public ApiResponse<TokenDTO> login(@RequestBody UserRequestDTO.LoginDTO request) {
        TokenDTO token = authService.login(request);
        return ApiResponse.onSuccess(token);
    }
}