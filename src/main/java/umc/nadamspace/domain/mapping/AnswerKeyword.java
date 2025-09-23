package umc.nadamspace.domain.mapping;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.nadamspace.domain.Answer;
import umc.nadamspace.domain.Keyword;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_keyword_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    @Builder
    public AnswerKeyword(Answer answer, Keyword keyword) {
        this.answer = answer;
        this.keyword = keyword;
    }

    //== 연관관계 편의 메서드 ==//
    // Answer 엔티티에서 호출하기 위한 Setter
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}