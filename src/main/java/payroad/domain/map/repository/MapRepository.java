package payroad.domain.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payroad.domain.map.Map;

public interface MapRepository extends JpaRepository<Map,Long> {

}
