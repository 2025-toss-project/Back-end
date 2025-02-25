package payroad.domain.map;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.awt.Point;
import lombok.NoArgsConstructor;
import payroad.domain.common.BaseEntity;

@Entity
@NoArgsConstructor
public class Map extends BaseEntity {

    @Id
    @Column(name = "map_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Point location;

    private String name;
}
