package umc.nadamspace.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.nadamspace.domain.common.BaseEntity;
import umc.nadamspace.domain.mapping.AnswerKeyword;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    // 어떤 일기에 속한 답변인지 (필수)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    // 어떤 질문에 대한 답변인지 (필수)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // 텍스트로 작성된 답변 내용
    @Lob
    private String answerBody;

    // 키워드 선택형 답변의 경우, 선택된 키워드 목록
    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerKeyword> answerKeywords = new ArrayList<>();

    // 빌더: 객체 생성을 위한 패턴
    @Builder
    public Answer(Diary diary, Question question, String answerBody) {
        this.diary = diary;
        this.question = question;
        this.answerBody = answerBody;
    }

    //== 연관관계 편의 메서드 ==//
    public void addAnswerKeyword(AnswerKeyword answerKeyword) {
        this.answerKeywords.add(answerKeyword);
        answerKeyword.setAnswer(this); // AnswerKeyword 엔티티에 setAnswer 메서드가 있다고 가정
    }
}