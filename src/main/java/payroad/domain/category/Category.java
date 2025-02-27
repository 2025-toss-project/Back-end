package payroad.domain.category;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import payroad.domain.common.BaseEntity;

@Entity
@NoArgsConstructor
@Getter
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    private String name;

}
