package umc.nadamspace.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import umc.nadamspace.domain.MonthlySummary;
import umc.nadamspace.domain.User;

import java.util.Optional;

public interface MonthlySummaryRepository extends JpaRepository<MonthlySummary, Long> {

    Optional<MonthlySummary> findByUserAndYearMonth(User user, String yearMonth);
}
