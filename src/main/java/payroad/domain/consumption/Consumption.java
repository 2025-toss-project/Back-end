package payroad.domain.consumption;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import payroad.domain.category.Category;
import payroad.domain.common.BaseEntity;
import payroad.domain.map.MapEntity;
import payroad.domain.member.Member;

@Entity
@NoArgsConstructor
@Getter
public class Consumption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "consumption_id")
    private Long id;

    @Setter
    private Integer price;

    @Setter
    private String details;

    @Setter
    private LocalDate date;

    @Setter
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "map_id")
    private MapEntity mapEntity;

    @Builder
    private Consumption(Long id, Integer price, String details, LocalDate date, Category category,
        Member member,
        MapEntity mapEntity) {
        this.id = id;
        this.price = price;
        this.details = details;
        this.date = date;
        this.category = category;
        this.member = member;
        this.mapEntity = mapEntity;
    }

    @Override
    public String toString() {
        return "Consumption [id=" + id + ", price=" + price + ", details=" + details + ", date=" + date;
    }

}
