package umc.nadamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.nadamspace.domain.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
