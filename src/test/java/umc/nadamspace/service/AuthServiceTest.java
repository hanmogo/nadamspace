package umc.nadamspace.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import umc.nadamspace.config.security.JwtTokenProvider;
import umc.nadamspace.domain.User;
import umc.nadamspace.dto.UserRequestDTO;
import umc.nadamspace.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static umc.nadamspace.domain.enums.Gender.MALE;

@ExtendWith(MockitoExtension.class) // Mockito를 사용한 단위 테스트를 위한 어노테이션
class AuthServiceTest {

    @InjectMocks
    private AuthService authService; // 테스트 대상 클래스. @Mock 객체들을 주입받습니다.

    @Mock
    private UserRepository userRepository; // 가짜 UserRepository 객체

    @Mock
    private PasswordEncoder passwordEncoder; // 가짜 PasswordEncoder 객체

    @Mock
    private JwtTokenProvider jwtTokenProvider; // 가짜 JwtTokenProvider 객체

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUp_success() {
        // given (주어진 상황)
        UserRequestDTO.SignUpDTO request = new UserRequestDTO.SignUpDTO("test@example.com", "password123", "테스트", "010-0000-0000", "20020603", MALE);
        User mockUser = User.builder().email(request.getEmail()).name(request.getName()).password("hashed_password").build();

        // userRepository.findByEmail이 호출되면, 비어있는 Optional을 반환하도록 설정 (중복 이메일 없음)
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        // passwordEncoder.encode가 호출되면, "hashed_password"를 반환하도록 설정
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashed_password");
        // userRepository.save가 호출되면, 가짜 User 객체를 반환하도록 설정
        when(userRepository.save(any(User.class))).thenReturn(mockUser);


        // when (무엇을 할 때)
        authService.signUp(request);


        // then (결과 확인)
        // 특별한 예외가 발생하지 않으면 성공으로 간주 (void 반환이므로)
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 이메일 중복")
    void signUp_fail_when_email_is_duplicated() {
        // given (주어진 상황)
        UserRequestDTO.SignUpDTO request = new UserRequestDTO.SignUpDTO("test@example.com", "password123", "테스트", "010-0000-0000", "20020603", MALE);

        // userRepository.findByEmail이 호출되면, 이미 User가 존재한다고 설정
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(User.builder().build()));

        // when & then (무엇을 할 때 & 결과 확인)
        // authService.signUp을 실행했을 때, IllegalArgumentException이 발생하는지 검증
        assertThrows(IllegalArgumentException.class, () -> {
            authService.signUp(request);
        });
    }
}