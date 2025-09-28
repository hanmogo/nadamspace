package umc.nadamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.nadamspace.domain.Tag;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    // 특정 사용자의 특정 기간 동안 가장 많이 사용된 태그 ID를 조회하는 쿼리 (상위 1개)
    @Query("SELECT dt.tag.id FROM DiaryTag dt " +
            "WHERE dt.diary.user.id = :userId AND dt.diary.createdAt BETWEEN :start AND :end " +
            "GROUP BY dt.tag.id ORDER BY COUNT(dt.tag.id) DESC LIMIT 1")
    Optional<Long> findMostFrequentTagIdByUserIdAndPeriod(@Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
