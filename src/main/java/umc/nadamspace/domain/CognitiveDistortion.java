package umc.nadamspace.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CognitiveDistortion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "distortion_id")
    private Long id;

    // 인지 왜곡의 이름 (e.g., "흑백논리적 사고", "과잉 일반화")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    // 인지 왜곡에 대한 상세 설명
    @Lob // TEXT 타입으로 매핑
    @Column(nullable = false)
    private String description;

    @Builder
    public CognitiveDistortion(String name, String description) {
        this.name = name;
        this.description = description;
    }
}