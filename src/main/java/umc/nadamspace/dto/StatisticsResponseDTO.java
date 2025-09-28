package umc.nadamspace.dto;

import lombok.Builder;
import lombok.Getter;
import umc.nadamspace.domain.MonthlySummary;


@Getter
@Builder
public class StatisticsResponseDTO {
    private String yearMonth;
    private int diaryCount;
    private String emotionStats; // JSON 형태의 감정 통계
    private String mostFrequentTagName;

    public static StatisticsResponseDTO from(MonthlySummary summary) {
        return StatisticsResponseDTO.builder()
                .yearMonth(summary.getYearMonth())
                .diaryCount(summary.getDiaryCount())
                .emotionStats(summary.getEmotionStats())
                .mostFrequentTagName(summary.getMostFrequentTag() != null ? summary.getMostFrequentTag().getName() : null)
                .build();
    }
}
