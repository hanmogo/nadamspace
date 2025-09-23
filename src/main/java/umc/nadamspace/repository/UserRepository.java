package umc.nadamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.nadamspace.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자를 찾는 기능을 추가 (Spring Data JPA가 메서드 이름을 보고 쿼리를 자동 생성)
    Optional<User> findByEmail(String email);

//    //카카오 구현하면 이거
//    /**
//     * 소셜 ID로 사용자를 찾는 메서드
//     * @param socialId 카카오에서 제공하는 고유 ID
//     * @return Optional<User>
//     */
//    Optional<User> findBySocialId(String socialId);
}
