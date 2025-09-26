package umc.nadamspace.domain.mapping;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.nadamspace.domain.Diary;
import umc.nadamspace.domain.Tag;

@Entity
@NoArgsConstructor
@Getter
public class DiaryTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Builder
    public DiaryTag(Diary diary, Tag tag) {
        this.diary = diary;
        this.tag = tag;
    }
}
