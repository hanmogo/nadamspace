package umc.nadamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.nadamspace.domain.Diary;

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
}
