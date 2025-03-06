package payroad.domain.member;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.locationtech.jts.geom.Point;
import lombok.NoArgsConstructor;
import payroad.domain.common.BaseEntity;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Setter
    private String password;

    private Type type;

    private AgeGroup ageGroup;

    @Column(nullable = false, columnDefinition = "POINT SRID 4326")
    private Point myLocation;

    @Builder
    public Member(String email, String nickname, String password, AgeGroup ageGroup, Type type,Point myLocation) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.ageGroup = ageGroup;
        this.type = type;
        this.myLocation = myLocation;
    }

}
