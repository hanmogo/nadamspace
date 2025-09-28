package umc.nadamspace.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CalendarDTO {

    private LocalDate date;
    private String emotionColor; //해당 날짜의 대표 갑정 색상
}
