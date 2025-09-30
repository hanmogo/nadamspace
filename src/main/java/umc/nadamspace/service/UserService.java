package umc.nadamspace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.nadamspace.domain.User;
import umc.nadamspace.domain.common.GeneralException;
import umc.nadamspace.domain.enums.ErrorCode;
import umc.nadamspace.dto.UserRequestDTO;
import umc.nadamspace.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션으로 설정
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    /**
     * 사용자 ID로 User 엔티티를 조회하는 메서드
     * @param userId 조회할 사용자의 ID
     * @return User 엔티티
     */
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다: " + userId));
    }


    /**
     * 내 정보 수정 API [PATCH /api/users/me]와 관련됨
     * @param userId 현재 로그인한 사용자 ID
     * @param request 수정할 프로필 정보
     * @return 수정된 User 엔티티
     */
    public User updateProfile(Long userId, UserRequestDTO.UpdateProfileDTO request) {
        User user = findUserById(userId);
        user.updateProfile(request.getName());
        return user; // Dirty checking으로 자동 업데이트
    }

    /**
     * 비밀번호 변경 API [PATCH /api/users/me/password]와 관련됨
     * @param userId 현재 로그인한 사용자 ID
     * @param request 현재 비밀번호와 새 비밀번호
     */
    public void changePassword(Long userId, UserRequestDTO.ChangePasswordDTO request) {
        User user = findUserById(userId);
        // 현재 비밀번호가 맞는지 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new GeneralException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        // 새 비밀번호를 암호화하여 변경
        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    /**
     * 회원 탈퇴 API [DELETE /api/users/me]와 관련됨
     * @param userId 현재 로그인한 사용자 ID
     */
    public void withdraw(Long userId) {
        User user = findUserById(userId);
        userRepository.delete(user);
    }
}