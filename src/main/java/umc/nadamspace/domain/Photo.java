package umc.nadamspace.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.nadamspace.domain.common.BaseEntity;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String photoUrl; // S3 등 파일 스토리지에 저장된 이미지 URL


    @Builder
    public Photo(String photoUrl,  Diary diary) {
        this.photoUrl = photoUrl;
        this.diary = diary;

    }
}