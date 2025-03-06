package payroad.domain.map;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import org.locationtech.jts.geom.Point;
import payroad.domain.common.BaseEntity;

@Entity
@NoArgsConstructor
@Getter
public class MapEntity extends BaseEntity {

    @Id
    @Column(name = "map_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "POINT SRID 4326")
    private Point location;

    private String name;

    @Builder
    public MapEntity(Point location, String name) {
        this.location = location;
        this.name = name;
    }
}
