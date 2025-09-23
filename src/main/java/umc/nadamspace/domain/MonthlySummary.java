package umc.nadamspace.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.nadamspace.domain.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonthlySummary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monthly_summary_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 요약 대상 년월 ( "2025-09")
    @Column(nullable = false, length = 7)
    private String yearMonth;

    // 해당 월에 작성한 총 일기 수
    @Column(nullable = false)
    private Integer diaryCount;

    // 해당 월의 감정 통계 정보 (JSON 형태)
    @Lob
    @Column(nullable = false)
    private String emotionStats;

    // 해당 월에 가장 많이 사용된 태그
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "most_frequent_tag_id")
    private Tag mostFrequentTag;

    @Builder
    public MonthlySummary(User user, String yearMonth, Integer diaryCount, String emotionStats, Tag mostFrequentTag) {
        this.user = user;
        this.yearMonth = yearMonth;
        this.diaryCount = diaryCount;
        this.emotionStats = emotionStats;
        this.mostFrequentTag = mostFrequentTag;
    }
}

