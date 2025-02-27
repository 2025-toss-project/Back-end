package payroad.domain.budget;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import payroad.domain.category.Category;
import payroad.domain.common.BaseEntity;
import payroad.domain.member.Member;


@Entity
@NoArgsConstructor
public class Budget extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "budget_id")
    @Getter
    private Long id;

    @Column(nullable = false)
    @Getter
    @Setter
    private int price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    @Getter
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    @Getter
    private Category category;

    @Builder
    public Budget(int price, Member member, Category category) {
        this.price = price;
        this.member = member;
        this.category = category;
    }

}
