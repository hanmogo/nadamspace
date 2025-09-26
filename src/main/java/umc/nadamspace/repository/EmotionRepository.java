package umc.nadamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.nadamspace.domain.Emotion;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
}
