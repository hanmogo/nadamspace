package umc.nadamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.nadamspace.domain.Diary;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
