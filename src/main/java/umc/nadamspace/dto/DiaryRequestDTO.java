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
}