package umc.nadamspace.dto;

import lombok.Builder;
import lombok.Getter;
import umc.nadamspace.domain.DiaryAnalysis;


@Getter
@Builder
public class DiaryAnalysisResponseDTO {

    private Long analysisId;
    private String aiComment;
    private DistortionDTO suggestedDistortion1;
    private DistortionDTO suggestedDistortion2;

    @Getter
    @Builder
    public static class DistortionDTO {
        private String name;
        private String description;
    }

    public static DiaryAnalysisResponseDTO from(DiaryAnalysis analysis) {
        // 인지 왜곡 정보가 null일 경우를 대비한 처리
        DistortionDTO d1 = analysis.getSuggestedDistortion1() != null ?
                DistortionDTO.builder()
                        .name(analysis.getSuggestedDistortion1().getName())
                        .description(analysis.getSuggestedDistortion1().getDescription())
                        .build() : null;

        DistortionDTO d2 = analysis.getSuggestedDistortion2() != null ?
                DistortionDTO.builder()
                        .name(analysis.getSuggestedDistortion2().getName())
                        .description(analysis.getSuggestedDistortion2().getDescription())
                        .build() : null;

        return DiaryAnalysisResponseDTO.builder()
                .analysisId(analysis.getId())
                .aiComment(analysis.getAiComment())
                .suggestedDistortion1(d1)
                .suggestedDistortion2(d2)
                .build();
    }
}
