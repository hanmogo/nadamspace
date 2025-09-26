package umc.nadamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import umc.nadamspace.domain.Diary;
import umc.nadamspace.domain.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    /**
     * 특정 Diary에 속한 모든 Photo들을 삭제
     * @param diary 삭제할 사진들이 속한 일기 엔티티
     */
    @Transactional
    void deleteAllByDiary(Diary diary);
}
