package umc.nadamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.nadamspace.domain.Diary;
import umc.nadamspace.domain.DiaryAnalysis;

import java.util.Optional;


public interface DiaryAnalysisRepository extends JpaRepository<DiaryAnalysis, Long> {

    Optional<DiaryAnalysis> findByDiaryId(Long diaryId);
}
