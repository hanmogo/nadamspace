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
public class DiaryAnalysis extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_analysis_id")
    private Long id;

    @Lob
    private String aiComment;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suggested_distortion_id_1")
    private CognitiveDistortion suggestedDistortion1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suggested_distortion_id_2")
    private CognitiveDistortion suggestedDistortion2;

    @Builder
    public DiaryAnalysis(Diary diary, String aiComment, CognitiveDistortion suggestedDistortion1, CognitiveDistortion suggestedDistortion2) {
        this.diary = diary;
        this.aiComment = aiComment;
        this.suggestedDistortion1 = suggestedDistortion1;
        this.suggestedDistortion2 = suggestedDistortion2;
    }
}
