package payroad.domain.map.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import payroad.domain.map.MapEntity;

public interface MapRepository extends JpaRepository<MapEntity, Long> {

    @Query(value = "SELECT * FROM map " +
        "WHERE ST_Equals(location, ST_GeomFromText(:point, 4326))",
        nativeQuery = true)
    Optional<MapEntity> findMapByPoint(@Param("point") String point);

}