package umc.nadamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.nadamspace.domain.Diary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findAllByUserIdOrderByCreatedAtDesc(Long userId);


    // diaryId로 일기를 조회할 때, 연관된 모든 데이터를 fetch join으로 한번에 가져옴
    @Query("SELECT d FROM Diary d " +
            "LEFT JOIN FETCH d.diaryEmotions de " +
            "LEFT JOIN FETCH de.emotion " +
            "LEFT JOIN FETCH d.diaryTags dt " +
            "LEFT JOIN FETCH dt.tag " +
            "LEFT JOIN FETCH d.photos " + // Photo 엔티티가 있다면 추가
            "WHERE d.id = :diaryId")
    Optional<Diary> findByIdWithDetails(@Param("diaryId") Long diaryId);


    @Query("SELECT d FROM Diary d " +
            "LEFT JOIN FETCH d.diaryEmotions de " +
            "LEFT JOIN FETCH de.emotion " +
            "WHERE d.user.id = :userId AND d.createdAt >= :startDate AND d.createdAt < :endDate")
    List<Diary> findDiariesForCalendar(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 특정 사용자의 특정 기간 동안 작성된 일기 개수를 세는 쿼리
    Long countByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    // 특정 사용자의 특정 기간 동안 가장 많이 사용된 감정들을 조회하는 쿼리
    @Query("SELECT de.emotion.id, COUNT(de.emotion.id) as count " +
            "FROM DiaryEmotion de " +
            "WHERE de.diary.user.id = :userId AND de.diary.createdAt BETWEEN :start AND :end " +
            "GROUP BY de.emotion.id ORDER BY count DESC")
    List<Object[]> findTopEmotionsByUserIdAndPeriod(@Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}


