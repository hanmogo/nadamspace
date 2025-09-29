package umc.nadamspace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.nadamspace.config.security.JwtTokenProvider;
import umc.nadamspace.domain.User;
import umc.nadamspace.dto.TokenDTO;
import umc.nadamspace.dto.UserRequestDTO;
import umc.nadamspace.repository.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long signUp(UserRequestDTO.SignUpDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // 날짜 형식을 지정합니다
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        // DTO에서 받은 생년월일 문자열을 LocalDate 객체로 변환합니다.
        LocalDate birthdate = LocalDate.parse(request.getBirthdate(), formatter);

        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(hashedPassword)
                .phoneNum(request.getPhoneNumber())
                .birthdate(birthdate)
                .gender(request.getGender())
                .build();

        return userRepository.save(newUser).getId();
    }

    public TokenDTO login(UserRequestDTO.LoginDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 암호화된 비밀번호와 입력된 비밀번호를 비교
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 로그인 성공 시 JWT 생성
        return jwtTokenProvider.generateToken(user.getId());
    }
}