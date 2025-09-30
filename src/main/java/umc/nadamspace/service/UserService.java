package umc.nadamspace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.nadamspace.domain.User;
import umc.nadamspace.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션으로 설정
public class UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 ID로 User 엔티티를 조회하는 메서드
     * @param userId 조회할 사용자의 ID
     * @return User 엔티티
     */
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다: " + userId));
    }

}