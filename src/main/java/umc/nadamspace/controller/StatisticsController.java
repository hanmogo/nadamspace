package umc.nadamspace.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.nadamspace.dto.ApiResponse;
import umc.nadamspace.dto.StatisticsResponseDTO;
import umc.nadamspace.service.StatisticsService;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/summary")
    public ApiResponse<StatisticsResponseDTO> getSummary(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long currentUserId = Long.valueOf(userDetails.getUsername());
        String yearMonth = String.format("%d-%02d", year, month);

        StatisticsResponseDTO summary = statisticsService.getMonthlySummary(currentUserId, yearMonth);
        return ApiResponse.onSuccess(summary);
    }
}