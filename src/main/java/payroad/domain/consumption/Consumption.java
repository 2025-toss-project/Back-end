package payroad.domain.consumption;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import payroad.domain.category.Category;
import payroad.domain.common.BaseEntity;
import payroad.domain.map.Map;
import payroad.domain.member.Member;

@Entity
@NoArgsConstructor
public class Consumption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "consumption_id")
    @Getter
    private Long id;

    private Integer price;

    private String details;

    private Date date;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "map_id")
    private Map map;

    @Builder
    private Consumption(Integer price, String details, Date date, Category category, Member member,
        Map map) {
        this.price = price;
        this.details = details;
        this.date = date;
        this.category = category;
        this.member = member;
        this.map = map;
    }

}
