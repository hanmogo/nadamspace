package umc.nadamspace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.nadamspace.domain.User;
import umc.nadamspace.dto.StatisticsResponseDTO;
import umc.nadamspace.repository.MonthlySummaryRepository;
import umc.nadamspace.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final MonthlySummaryRepository monthlySummaryRepository;
    private final UserRepository userRepository;

    public StatisticsResponseDTO getMonthlySummary(Long userId, String yearMonth) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return monthlySummaryRepository.findByUserAndYearMonth(user, yearMonth)
                .map(StatisticsResponseDTO::from)
                .orElse(null); // 통계 데이터가 아직 없으면 null 반환
    }
}