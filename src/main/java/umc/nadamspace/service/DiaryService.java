package umc.nadamspace.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.nadamspace.domain.*;
import umc.nadamspace.domain.enums.DiaryType;
import umc.nadamspace.domain.mapping.DiaryEmotion;
import umc.nadamspace.domain.mapping.DiaryTag;
import umc.nadamspace.dto.DiaryRequestDTO;
import umc.nadamspace.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final EmotionRepository emotionRepository;
    private final TagRepository tagRepository;
    private final PhotoRepository photoRepository;

    public Long createFreestyleDiary(Long userId, DiaryRequestDTO.CreateFreestyleDTO request) {
        // 1. 작성자 찾기
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다."));

        // 2. Diary 엔티티 생성 및 저장 (연관관계 주인인 Diary가 먼저 저장되어야 함)
        Diary newDiary = Diary.builder()
                .user(writer)
                .title(request.getTitle())
                .body(request.getBody())
                .diaryType(DiaryType.FREEFORM)
                .build();
        diaryRepository.save(newDiary);

        // 3. 감정(Emotion) 연결
        if (request.getEmotionId() != null && !request.getEmotionId().isEmpty()) {
            List<Emotion> emotions = emotionRepository.findAllById(request.getEmotionId());
            for (Emotion emotion : emotions) {
                DiaryEmotion diaryEmotion = DiaryEmotion.builder()
                        .diary(newDiary)
                        .emotion(emotion)
                        .build();
                // Diary 엔티티의 연관관계 편의 메서드를 통해 추가 (양방향일 경우)
                newDiary.addDiaryEmotion(diaryEmotion);
            }
        }

        // 4. 사진(Photo) 연결
        if (request.getPhotoUrls() != null && !request.getPhotoUrls().isEmpty()) {
            for (String photoUrl : request.getPhotoUrls()) {
                Photo newPhoto = Photo.builder()
                        .diary(newDiary)
                        .photoUrl(photoUrl)
                        .build();
                photoRepository.save(newPhoto); // Photo는 직접 저장
            }
        }

        // 5. 태그(Tag) 연결
        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            for (String tagName : request.getTagNames()) {
                // DB에서 태그를 찾거나, 없으면 새로 생성
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));

                DiaryTag diaryTag = DiaryTag.builder()
                        .diary(newDiary)
                        .tag(tag)
                        .build();
                // Diary 엔티티의 연관관계 편의 메서드를 통해 추가 (양방향일 경우)
                newDiary.addDiaryTag(diaryTag);
            }
        }

        return newDiary.getId();
    }

}
