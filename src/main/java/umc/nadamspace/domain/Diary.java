package umc.nadamspace.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.nadamspace.domain.common.BaseEntity;
import umc.nadamspace.domain.enums.DiaryType;
import umc.nadamspace.domain.mapping.DiaryEmotion;
import umc.nadamspace.domain.mapping.DiaryTag;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @Column(length = 254)
    private String title;

    @Lob
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaryType diaryType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryEmotion> diaryEmotions = new ArrayList<>();

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryTag> diaryTags = new ArrayList<>();


    //연관관계 편의 메서드
    public void addDiaryEmotion(DiaryEmotion diaryEmotion) {
        diaryEmotions.add(diaryEmotion);
    }

    public void addDiaryTag(DiaryTag diaryTag) {
        diaryTags.add(diaryTag);
    }
}
