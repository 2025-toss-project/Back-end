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

    // 시작과 끝 일자에 대해서 consumption의 값을 불러오는 쿼리를 날린다.
    @Query("SELECT c FROM Consumption c WHERE c.member = :member " +
        "AND (MONTH(c.date) > :startMonth " +
        "OR (MONTH(c.date) = :startMonth AND DAY(c.date) >= :startDay)) " +
        "AND (MONTH(c.date) < :endMonth " +
        "OR (MONTH(c.date) = :endMonth AND DAY(c.date) <= :endDay))")
    List<Consumption> findByMemberAndDateRange(
        @Param("member") Member member,
        @Param("startMonth") int startMonth,
        @Param("startDay") int startDay,
        @Param("endMonth") int endMonth,
        @Param("endDay") int endDay
    );

}
