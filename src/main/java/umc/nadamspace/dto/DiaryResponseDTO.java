package umc.nadamspace.dto;

import lombok.Builder;
import lombok.Getter;
import umc.nadamspace.domain.Answer;
import umc.nadamspace.domain.Diary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DiaryResponseDTO {

    // 목록의 각 아이템에 사용될 DTO
    @Getter
    @Builder
    public static class DiaryPreviewDTO {
        private Long diaryId;
        private String title;
        private LocalDate createdAt;

        // Entity를 DTO로 변환하는 정적 팩토리 메서드
        public static DiaryPreviewDTO from(Diary diary) {
            return DiaryPreviewDTO.builder()
                    .diaryId(diary.getId())
                    .title(diary.getTitle())
                    .createdAt(diary.getCreatedAt().toLocalDate()) // LocalDateTime에서 날짜만 추출
                    .build();
        }
    }

    // 상세 조회 응답에 사용될 DTO
    @Getter
    @Builder
    public static class DiaryDetailDTO {
        private Long diaryId;
        private String title;
        private String body;
        private LocalDateTime createdAt;
        private List<String> emotionNames; // 감정 이름 목록
        private List<String> tagNames;     // 태그 이름 목록
        private List<String> photoUrls;    // 사진 URL 목록

        public static DiaryDetailDTO from(Diary diary) {
            return DiaryDetailDTO.builder()
                    .diaryId(diary.getId())
                    .title(diary.getTitle())
                    .body(diary.getBody())
                    .createdAt(diary.getCreatedAt())
                    .emotionNames(diary.getDiaryEmotions().stream()
                            .map(diaryEmotion -> diaryEmotion.getEmotion().getName())
                            .collect(Collectors.toList()))
                    .tagNames(diary.getDiaryTags().stream()
                            .map(diaryTag -> diaryTag.getTag().getName())
                            .collect(Collectors.toList()))
                    .photoUrls(diary.getPhotos().stream() // Diary 엔티티에 getPhotos()가 있다고 가정
                            .map(photo -> photo.getPhotoUrl())
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    // 가이드형 일기 상세 조회 응답 DTO
    @Getter
    @Builder
    public static class GuidedDiaryDetailDTO {
        private Long diaryId;
        private LocalDateTime createdAt;
        private List<AnswerResponseDTO> answers;

        public static GuidedDiaryDetailDTO from(Diary diary) {
            List<AnswerResponseDTO> answerDTOs = diary.getAnswers().stream()
                    .map(AnswerResponseDTO::from)
                    .collect(Collectors.toList());

            return GuidedDiaryDetailDTO.builder()
                    .diaryId(diary.getId())
                    .createdAt(diary.getCreatedAt())
                    .answers(answerDTOs)
                    .build();
        }
    }

    // Answer의 내용을 담을 내부 DTO
    @Getter
    @Builder
    public static class AnswerResponseDTO {
        private String question;
        private String answerBody;
        private List<String> keywords;

        public static AnswerResponseDTO from(Answer answer) {
            List<String> keywordContents = answer.getAnswerKeywords().stream()
                    .map(answerKeyword -> answerKeyword.getKeyword().getContent())
                    .collect(Collectors.toList());

            return AnswerResponseDTO.builder()
                    .question(answer.getQuestion().getQuestionBody())
                    .answerBody(answer.getAnswerBody())
                    .keywords(keywordContents)
                    .build();
        }
    }
}



