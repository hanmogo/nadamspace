package umc.nadamspace.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Emotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emotion_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    // 상위 감정 (Self-referencing)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_emotion_id")
    private Emotion parent;

    @Column(nullable = false, length = 10)
    private String color;

    @Builder
    public Emotion(String name, Emotion parent, String color) {
        this.name = name;
        this.parent = parent;
        this.color = color;
    }
}