package payroad.domain.category.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import payroad.domain.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String categoryName);
}
