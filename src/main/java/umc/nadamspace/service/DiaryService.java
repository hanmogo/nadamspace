package umc.nadamspace.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.nadamspace.domain.*;
import umc.nadamspace.domain.enums.DiaryType;
import umc.nadamspace.domain.mapping.AnswerKeyword;
import umc.nadamspace.domain.mapping.DiaryEmotion;
import umc.nadamspace.domain.mapping.DiaryTag;
import umc.nadamspace.dto.CalendarDTO;
import umc.nadamspace.dto.DiaryAnalysisResponseDTO;
import umc.nadamspace.dto.DiaryRequestDTO;
import umc.nadamspace.dto.DiaryResponseDTO;
import umc.nadamspace.repository.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final EmotionRepository emotionRepository;
    private final TagRepository tagRepository;
    private final PhotoRepository photoRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final KeywordRepository keywordRepository;
    private final DiaryAnalysisRepository diaryAnalysisRepository;


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


    public List<DiaryResponseDTO.DiaryPreviewDTO> getDiaryList(Long userId) {

        // Repository를 통해 특정 사용자의 모든 일기를 최신순으로 조회
        List<Diary> diaryList = diaryRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        // 조회된 Diary 엔티티 목록을 DTO 목록으로 변환
        return diaryList.stream()
                .map(DiaryResponseDTO.DiaryPreviewDTO::from) // Stream의 map과 DTO의 from 메서드 활용
                .collect(Collectors.toList());
    }

    public DiaryResponseDTO.DiaryDetailDTO getDiaryDetail(Long diaryId) {

        Diary diary = diaryRepository.findByIdWithDetails(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 일기를 찾을 수 없습니다."));
        //조회된 Diary 엔티티 DTO로 변경
        return  DiaryResponseDTO.DiaryDetailDTO.from(diary);
    }


    public void updateDiary(Long userId, Long diaryId, DiaryRequestDTO.UpdateDTO request) {
        // 1. 수정할 일기를 DB에서 조회
        Diary diary = diaryRepository.findByIdWithDetails(diaryId) // 기존 Fetch Join 쿼리 재활용
                .orElseThrow(() -> new IllegalArgumentException("해당하는 일기를 찾을 수 없습니다."));

        // 2.현재 로그인한 사용자가 이 일기의 작성자가 맞는지 권한 확인
        if (!diary.getUser().getId().equals(userId)) {
            throw new SecurityException("일기를 수정할 권한이 없습니다.");
        }

        // 3. Entity 내부의 메서드를 통해 제목, 본문 등 기본 정보 업데이트
        diary.update(request.getTitle(), request.getBody());

        // 4. 연관관계 데이터감정, 태그, 사진 업데이트
        updateDiaryEmotions(diary, request.getEmotionIds());
        updateDiaryTags(diary, request.getTagNames());
        updatePhotos(diary, request.getPhotoUrls());

    }

    // --- 아래는 updateDiary 메서드 내부에서 사용할 private 헬퍼 메서드들 ---

    private void updateDiaryEmotions(Diary diary, List<Long> emotionIds) {
        diary.getDiaryEmotions().clear(); // orphanRemoval=true 옵션 덕분에 DB에서도 삭제됨
        if (emotionIds != null) {
            List<Emotion> emotions = emotionRepository.findAllById(emotionIds);
            for (Emotion emotion : emotions) {
                DiaryEmotion diaryEmotion = DiaryEmotion.builder().diary(diary).emotion(emotion).build();
                diary.addDiaryEmotion(diaryEmotion);
            }
        }
    }

    private void updateDiaryTags(Diary diary, List<String> tagNames) {
        diary.getDiaryTags().clear(); // orphanRemoval=true 옵션 덕분에 DB에서도 삭제됨
        if (tagNames != null) {
            for (String tagName : tagNames) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));
                DiaryTag diaryTag = DiaryTag.builder().diary(diary).tag(tag).build();
                diary.addDiaryTag(diaryTag);
            }
        }
    }

    private void updatePhotos(Diary diary, List<String> photoUrls) {

        photoRepository.deleteAllByDiary(diary); // Photo는 Diary에 종속되므로 직접 삭제 쿼리 실행
        if (photoUrls != null) {
            for (String photoUrl : photoUrls) {
                Photo newPhoto = Photo.builder().diary(diary).photoUrl(photoUrl).build();
                photoRepository.save(newPhoto);
            }
        }
    }

    public void deleteDiary(Long userId, Long diaryId) {
        // 1. 삭제할 일기를 DB에서 조회
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 일기를 찾을 수 없습니다."));

        // 2. 현재 로그인한 사용자가 이 일기의 작성자가 맞는지 권한 확인
        if (!diary.getUser().getId().equals(userId)) {
            throw new SecurityException("일기를 삭제할 권한이 없습니다.");
        }

        // 3. 일기 삭제
        diaryRepository.delete(diary);
    }

    public List<CalendarDTO> getCalendarInfo(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Diary> monthlyDiaries = diaryRepository.findDiariesForCalendar(userId, startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());

        return monthlyDiaries.stream()
                .collect(Collectors.groupingBy(
                        diary -> diary.getCreatedAt().toLocalDate(),
                        Collectors.minBy(Comparator.comparing(Diary::getCreatedAt))
                ))
                .values().stream()
                .map(optionalDiary -> optionalDiary.get())
                .filter(diary -> !diary.getDiaryEmotions().isEmpty()) // 감정이 비어있지 않은 일기만 필터링
                .map(diary -> {
                    //diary 객체는 무조건 감정을 하나 이상 가지고 있음
                    String color = diary.getDiaryEmotions().get(0).getEmotion().getColor();
                    return new CalendarDTO(diary.getCreatedAt().toLocalDate(), color);
                })
                .collect(Collectors.toList());
    }


    //Guideform
    public Long createGuideformDiary(Long userId, DiaryRequestDTO.CreateGuideformDTO request) {
        // 1. 사용자 조회
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다."));

        // 2. 부모 엔티티인 Diary (GUIDEFORM 타입) 생성 및 저장 -> diary_id 확보
        Diary newDiary = Diary.builder()
                .user(writer)
                .diaryType(DiaryType.GUIDEFORM)
                .build();
        diaryRepository.save(newDiary);

        // 3. 전달받은 답변 목록(answers)을 순회하며 Answer 엔티티 생성 및 저장
        List<Answer> savedAnswers = new ArrayList<>(); // 1. 결과를 담을 빈 리스트 생성

        for (DiaryRequestDTO.AnswerDTO answerDto : request.getAnswers()) {
            Question question = questionRepository.findById(answerDto.getQuestionId())
                    .orElseThrow(() -> new IllegalArgumentException("해당하는 질문을 찾을 수 없습니다."));

            Answer newAnswer = Answer.builder()
                    .diary(newDiary)
                    .question(question)
                    .answerBody(answerDto.getAnswerBody())
                    .build();

            if (answerDto.getKeywordIds() != null && !answerDto.getKeywordIds().isEmpty()) {
                List<Keyword> keywords = keywordRepository.findAllById(answerDto.getKeywordIds());
                for (Keyword keyword : keywords) {
                    AnswerKeyword answerKeyword = AnswerKeyword.builder()
                            .answer(newAnswer)
                            .keyword(keyword)
                            .build();

                    newAnswer.addAnswerKeyword(answerKeyword);
                }
            }

            // 2. Answer를 저장하고, DB에 저장되어 ID가 부여된 객체를 리스트에 추가
            savedAnswers.add(answerRepository.save(newAnswer));
        }
        // 4. 저장된 답변들을 바탕으로 분석 데이터 생성
        createAndSaveAnalysis(newDiary, savedAnswers); // savedAnswers는 저장된 Answer 엔티티 목록

        return newDiary.getId();
    }

    private void createAndSaveAnalysis(Diary diary, List<Answer> answers) {
        // 4-1. AI 코멘트 생성 (외부 LLM API 호출)
        // 실제로는 answers를 프롬프트로 가공하여 API 호출
        String aiComment = generateAiComment(answers);

        // 4-2. 인지 왜곡 패턴 분석
        // answers에서 키워드 답변을 찾아, 미리 정의된 로직에 따라 상위 2개 패턴 ID 결정
        CognitiveDistortion distortion1 = findTopCognitiveDistortion(answers);
        CognitiveDistortion distortion2 = findSecondCognitiveDistortion(answers);

        // 4-3. DiaryAnalysis 엔티티 생성 및 저장
        DiaryAnalysis analysis = DiaryAnalysis.builder()
                .diary(diary)
                .aiComment(aiComment)
                .suggestedDistortion1(distortion1)
                .suggestedDistortion2(distortion2)
                .build();

        diaryAnalysisRepository.save(analysis);
    }

    // 외부 LLM(GPT, Gemini 등) API를 호출하는 가상 메서드
    private String generateAiComment(List<Answer> answers) {
        // Spring의 WebClient나 RestTemplate을 사용하여 외부 API 호출
        // 1. answers 목록을 하나의 완성된 텍스트 프롬프트로 만듭니다.
        // 2. LLM API에 프롬프트를 보내고 응답을 받습니다.
        // 3. 받은 응답을 반환합니다.
        return "AI가 생성한 따뜻한 코멘트입니다."; // 임시 반환값
    }

    // 인지 왜곡 패턴을 찾는 가상 메서드
    private CognitiveDistortion findTopCognitiveDistortion(List<Answer> answers) {
        // 1. answers에서 키워드를 선택한 답변을 찾습니다.
        // 2. 선택된 Keyword ID들을 바탕으로 가장 많이 나타난 인지 왜곡 패턴을 분석합니다.
        // 3. CognitiveDistortion 엔티티를 반환합니다.
        return null; // 임시 반환값
    }

    private CognitiveDistortion findSecondCognitiveDistortion(List<Answer> answers) {
        //두번째 인지 왜곡 패턴 분석
        return null;
    }

    public DiaryAnalysisResponseDTO getDiaryAnalysis(Long userId, Long diaryId) {
        // 1. diaryId로 분석 결과를 조회합니다.
        DiaryAnalysis analysis = diaryAnalysisRepository.findByDiaryId(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기에 대한 분석 결과를 찾을 수 없습니다."));

        // 2. (핵심) 조회된 분석 결과가 현재 로그인한 사용자의 일기가 맞는지 확인합니다.
        if (!analysis.getDiary().getUser().getId().equals(userId)) {
            throw new SecurityException("해당 분석을 조회할 권한이 없습니다.");
        }

        // 3. 권한이 확인되면 DTO로 변환하여 반환합니다.
        return DiaryAnalysisResponseDTO.from(analysis);
    }
}
