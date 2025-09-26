package umc.nadamspace.domain.mapping;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import umc.nadamspace.domain.Diary;
import umc.nadamspace.domain.Emotion;

@Entity
@NoArgsConstructor
public class DiaryEmotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_id")
    private Emotion emotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Builder
    public DiaryEmotion(Diary diary, Emotion emotion) {
        this.diary = diary;
        this.emotion = emotion;
    }

}
