package umc.nadamspace.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import umc.nadamspace.dto.ApiResponse;
import umc.nadamspace.dto.DiaryAnalysisResponseDTO;
import umc.nadamspace.dto.DiaryRequestDTO;
import umc.nadamspace.dto.DiaryResponseDTO;
import umc.nadamspace.service.DiaryService;

import java.util.List;

@RestController
@RequestMapping("/api/diaries") // 이 컨트롤러의 모든 API는 /api/diaries 로 시작합니다.
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * 자유형 일기 작성 API
     * [POST] /api/diaries/freestyle
     */
    @PostMapping("/freestyle")
    public ApiResponse<Long> createFreestyleDiary(
            @RequestBody DiaryRequestDTO.CreateFreestyleDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {

        // @AuthenticationPrincipal 현재 로그인한 사용자의 정보를 가져옴
        // userDetails.getUsername()은 JwtTokenProvider에서 subject로 설정했던 userId를 문자열로 반환
        Long currentUserId = Long.valueOf(userDetails.getUsername());

        // Service를 호출하여 일기를 생성합니다.
        Long newDiaryId = diaryService.createFreestyleDiary(currentUserId, request);

        return ApiResponse.onSuccess(newDiaryId);
    }

    /**
     * 일기 목록 조회 API
     * [GET] /api/diaries
     */
    @GetMapping("")
    public ApiResponse<List<DiaryResponseDTO.DiaryPreviewDTO>> getDiaryList(
            @AuthenticationPrincipal UserDetails userDetails) {

        // 현재 로그인한 사용자의 ID를 가져옴
        Long currentUserId = Long.valueOf(userDetails.getUsername());

        // Service를 호출하여 일기 목록을 조회
        List<DiaryResponseDTO.DiaryPreviewDTO> diaryList = diaryService.getDiaryList(currentUserId);

        return ApiResponse.onSuccess(diaryList);
    }

    /**
     * 일기 상세 조회 API
     * [GET] /api/diaries/{diaryId}
     */
    @GetMapping("/{diaryId}")
    public ApiResponse<DiaryResponseDTO.DiaryDetailDTO> getDiaryDetail(
            @PathVariable(name = "diaryId") Long diaryId) {

        DiaryResponseDTO.DiaryDetailDTO diaryDetail = diaryService.getDiaryDetail(diaryId);
        return ApiResponse.onSuccess(diaryDetail);
    }

    /**
     * 일기 수정 API
     * [PUT] /api/diaries/{diaryId}
     */
    @PutMapping("/{diaryId}")
    public ApiResponse<String> updateDiary(
            @PathVariable(name = "diaryId") Long diaryId,
            @RequestBody DiaryRequestDTO.UpdateDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long currentUserId = Long.valueOf(userDetails.getUsername());
        diaryService.updateDiary(currentUserId, diaryId, request);

        return ApiResponse.onSuccess("일기가 수정되었습니다.");
    }

    /**
     * 일기 삭제 API
     * [DELETE] /api/diaries/{diaryId}
     */
    @DeleteMapping("/{diaryId}")
    public ApiResponse<String> deleteDiary(
            @PathVariable(name = "diaryId") Long diaryId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long currentUserId = Long.valueOf(userDetails.getUsername());
        diaryService.deleteDiary(currentUserId, diaryId);

        return ApiResponse.onSuccess("일기가 삭제되었습니다.");
    }

    /**
     * 가이드형 일기 작성 API
     * [POST] /api/diaries/guideform
     */
    @PostMapping("/guideform")
    public ApiResponse<Long> createGuideformDiary(
            @RequestBody DiaryRequestDTO.CreateGuideformDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long currentUserId = Long.valueOf(userDetails.getUsername());
        Long newDiaryId = diaryService.createGuideformDiary(currentUserId, request);

        return ApiResponse.onSuccess(newDiaryId);
    }

    /**
     * 일기 분석 결과 조회 API
     * [GET] /api/diaries/{diaryId}/analysis
     */
    @GetMapping("/{diaryId}/analysis")
    public ApiResponse<DiaryAnalysisResponseDTO> getDiaryAnalysis(
            @PathVariable("diaryId") Long diaryId) {

        // TODO: 현재 로그인한 사용자가 해당 일기의 주인인지 확인하는 로직 추가하면 좋음

        DiaryAnalysisResponseDTO analysisResponse = diaryService.getDiaryAnalysis(diaryId);
        return ApiResponse.onSuccess(analysisResponse);
    }
}

