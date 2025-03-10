package payroad.domain.consumption.repository;

import java.time.LocalDate;
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
        "AND c.date BETWEEN :startDate AND :endDate ")
    List<Consumption> findByMemberAndDateRange(
        @Param("member") Member member,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
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

    // 특정 Member ID를 기반으로 소비 내역 찾기 (JPQL 사용)
    @Query("SELECT c FROM Consumption c WHERE c.member.id = :memberId")
    List<Consumption> findByMemberId(@Param("memberId") Long memberId);


    @Query(value = "SELECT c.* FROM consumption c " +
        "JOIN map_entity m ON c.map_id = m.map_id " +
        "WHERE c.member_id = :memberId " +
        "AND ST_Distance_Sphere(m.location, ST_GeomFromText(:point, 4326)) <= :radius * 1000",
        nativeQuery = true)
    List<Consumption> findConsumptionsByMemberAndRadius(
        @Param("memberId") Long memberId,
        @Param("point") String point,  // "POINT(lng lat)" 형식의 문자열
        @Param("radius") double radius);

    // 자신을 제외 다른 사람들의 지출내역을 가져 오는 쿼리
    @Query(value = "SELECT * FROM consumption WHERE member_id"
        + " IN ( SELECT member_id FROM "
        + "( SELECT DISTINCT m.member_id FROM member m WHERE m.member_id != :memberId AND m.type = :type  ORDER BY RAND() LIMIT 10 ) AS temp )"
        , nativeQuery = true)
    List<Consumption> findByNotMember(
        @Param("memberId") Long memberId,
        @Param("type") int type);
}
