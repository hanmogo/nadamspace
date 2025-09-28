package umc.nadamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.nadamspace.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
