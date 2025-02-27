package payroad.domain.consumption.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import payroad.domain.consumption.Consumption;
import payroad.domain.member.Member;

public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {

    @Query("SELECT c.category.name AS category, SUM(c.price) AS totalPrice " +
        "FROM Consumption c " +
        "WHERE c.member = :member " +
        "AND MONTH(c.date) = :month " +
        "AND YEAR(c.date) = :year " +
        "GROUP BY c.category.id")
    List<Object[]> findConsumptionByMemberAndMonth(
        @Param("member") Member member,
        @Param("month") int month,
        @Param("year") int year
    );

}
