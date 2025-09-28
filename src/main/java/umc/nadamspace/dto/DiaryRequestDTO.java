package umc.nadamspace.dto;

import lombok.Getter;

import java.util.List;

public class DiaryRequestDTO {

    @Getter
    public static class CreateFreestyleDTO {
        private String title;
        private String body;
        private List<Long> emotionId;
        private List<String> photoUrls;
        private List<String> tagNames;
    }

    @Getter
    public static class UpdateDTO {
        private String title;
        private String body;
        private List<Long> emotionIds;
        private List<String> photoUrls;
        private List<String> tagNames;
    }

    @Getter
    public static class CreateGuideformDTO {
        // 여러 개의 답변으로 구성된 리스트
        private List<AnswerDTO> answers;
    }

    // 개별 답변의 내용을 담는 DTO
    @Getter
    public static class AnswerDTO {
        private Long questionId;      // 어떤 질문(Question)에 대한 답변인지 ID
        private String answerBody;    // 텍스트 형태의 답변 내용
        private List<Long> keywordIds; // 키워드 선택형 답변의 경우, 선택된 Keyword ID 목록
    }
}