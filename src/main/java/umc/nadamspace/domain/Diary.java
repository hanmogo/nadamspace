package umc.nadamspace.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryEmotion> diaryEmotions = new ArrayList<>();

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryTag> diaryTags = new ArrayList<>();

    @OneToMany(mappedBy = "diary")
    private List<Answer> answers = new ArrayList<>();

    @OneToOne(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private DiaryAnalysis analysis;


    @Builder
    public Diary(String title, String body, DiaryType diaryType, User user) {
        this.title = title;
        this.body = body;
        this.diaryType = diaryType;
        this.user = user;

    }


    //비즈니스 로직 메서드
    public void update(String title, String body){
        this.title = title;
        this.body = body;
    }

    //연관관계 편의 메서드
    public void addDiaryEmotion(DiaryEmotion diaryEmotion) {
        diaryEmotions.add(diaryEmotion);
    }

    public void addDiaryTag(DiaryTag diaryTag) {
        diaryTags.add(diaryTag);
    }

    public void setAnalysis(DiaryAnalysis analysis) {
        this.analysis = analysis;
    }
}
