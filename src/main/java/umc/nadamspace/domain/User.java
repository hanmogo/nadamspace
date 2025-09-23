package umc.nadamspace.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.nadamspace.domain.common.BaseEntity;
import umc.nadamspace.domain.enums.Gender;
import umc.nadamspace.domain.enums.State;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;


    @Enumerated(EnumType.STRING)
    private State state;


    @Column(unique = true)
    private String phoneNum;

    private LocalDate birthdate;

    private LocalDateTime inactive_date;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Diary> diaries = new ArrayList<>();

    @Builder
    public User(String name, String email, String password, Gender gender, String phoneNum, LocalDate birthdate, LocalDateTime inactive_date) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.state = State.ACTIVE;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.birthdate = birthdate;
        this.inactive_date = inactive_date;
    }
}
