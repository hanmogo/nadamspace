package umc.nadamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.nadamspace.domain.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
}
