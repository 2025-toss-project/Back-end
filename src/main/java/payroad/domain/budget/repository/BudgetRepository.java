package payroad.domain.budget.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import payroad.domain.budget.Budget;
import payroad.domain.member.Member;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<List<Budget>> findByMemberId(Member member);
}
