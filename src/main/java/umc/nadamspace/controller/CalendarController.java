package umc.nadamspace.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.nadamspace.dto.ApiResponse;
import umc.nadamspace.dto.CalendarDTO;
import umc.nadamspace.service.DiaryService;

import java.util.List;
@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private DiaryService diaryService;

    /**
     * 월별 감정 캘린더 정보 조회 API
     * [GET] /api/calendar?year=2025&month=9
     */
    @GetMapping("")
    public ApiResponse<List<CalendarDTO>> getCalendarInfo(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long currentUserId = Long.valueOf(userDetails.getUsername());
        List<CalendarDTO> calendarInfo = diaryService.getCalendarInfo(currentUserId, year, month);
        return ApiResponse.onSuccess(calendarInfo);
    }
}
