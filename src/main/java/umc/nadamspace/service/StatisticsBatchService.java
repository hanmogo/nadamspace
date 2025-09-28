package umc.nadamspace.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.nadamspace.domain.MonthlySummary;
import umc.nadamspace.domain.Tag;
import umc.nadamspace.domain.User;
import umc.nadamspace.repository.DiaryRepository;
import umc.nadamspace.repository.MonthlySummaryRepository;
import umc.nadamspace.repository.TagRepository;
import umc.nadamspace.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsBatchService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final TagRepository tagRepository;
    private final MonthlySummaryRepository monthlySummaryRepository;
    private final ObjectMapper objectMapper; // JSON 변환을 위한 ObjectMapper

    // 매일 새벽 4시에 실행
    @Scheduled(cron = "0 0 4 * * *")
    @Transactional
    public void calculateMonthlySummaries() {
        log.info("월별 통계 계산 배치 작업을 시작합니다.");

        YearMonth targetMonth = YearMonth.from(LocalDate.now().minusDays(1));
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            calculateAndSaveSummaryForUser(user, targetMonth);
        }
        log.info("월별 통계 계산 배치 작업을 완료했습니다.");
    }

    private void calculateAndSaveSummaryForUser(User user, YearMonth yearMonth) {
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay();
        String yearMonthStr = yearMonth.toString();

        // 1. 일기 개수 계산
        Long diaryCount = diaryRepository.countByUserIdAndCreatedAtBetween(user.getId(), start, end);

        // 일기를 하나도 쓰지 않았으면 통계 x
        if (diaryCount == 0) {
            monthlySummaryRepository.findByUserAndYearMonth(user, yearMonthStr).ifPresent(monthlySummaryRepository::delete);
            return;
        }

        // 2. 감정 통계 계산
        List<Object[]> topEmotions = diaryRepository.findTopEmotionsByUserIdAndPeriod(user.getId(), start, end);
        Map<String, Long> emotionCounts = new HashMap<>();
        Long mostFrequentEmotionId = -1L;
        if (!topEmotions.isEmpty()) {
            mostFrequentEmotionId = (Long) topEmotions.get(0)[0];
            for (Object[] result : topEmotions) {
                emotionCounts.put(String.valueOf(result[0]), (Long) result[1]);
            }
        }

        // 감정 통계를 JSON 문자열로 변환
        String emotionStatsJson = convertMapToJson(Map.of("emotion_counts", emotionCounts, "most_frequent_emotion_id", mostFrequentEmotionId));

        // 3. 가장 많이 사용된 태그 조회
        Optional<Long> mostFrequentTagId = tagRepository.findMostFrequentTagIdByUserIdAndPeriod(user.getId(), start, end);
        Tag mostFrequentTag = mostFrequentTagId.flatMap(tagRepository::findById).orElse(null);

        // 4. MonthlySummary 엔티티 조회 또는 생성 후, 계산된 값으로 업데이트
        MonthlySummary summary = monthlySummaryRepository.findByUserAndYearMonth(user, yearMonthStr)
                .orElseGet(() -> MonthlySummary.builder()
                        .user(user)
                        .yearMonth(yearMonthStr)
                        .build());

        summary.updateStats(diaryCount.intValue(), emotionStatsJson, mostFrequentTag); // 엔티티 업데이트
        monthlySummaryRepository.save(summary);
    }

    // Map을 JSON 문자열로 변환하는 헬퍼 메서드
    private String convertMapToJson(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert map to JSON", e);
            return "{}";
        }
    }
}