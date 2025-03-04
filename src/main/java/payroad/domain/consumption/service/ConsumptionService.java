package payroad.domain.consumption.service;


import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payroad.domain.category.Category;
import payroad.domain.consumption.Consumption;
import payroad.domain.consumption.dto.ConsumptionConverter;
import payroad.domain.consumption.dto.ConsumptionRequest;
import payroad.domain.consumption.dto.ConsumptionResponse;
import payroad.domain.consumption.dto.ConsumptionResponse.ConsumptionInfoDTOList;
import payroad.domain.consumption.repository.ConsumptionRepository;
import payroad.domain.map.MapEntity;
import payroad.domain.map.dto.MapConveter;
import payroad.domain.map.repository.MapRepository;
import payroad.domain.member.Member;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsumptionService {

    private final ConsumptionRepository consumptionRepository;
    private final MapRepository mapRepository;

    public ConsumptionResponse.ConsumptionInfoDTOList getConsumptionInfo(
        Member member,
        int startMonth,
        int startDay,
        int endMonth,
        int endDay
    ) {
        List<Consumption> byMemberAndDateRange = consumptionRepository.findByMemberAndDateRange(
            member, startMonth, startDay, endMonth, endDay);
        ConsumptionInfoDTOList comsumptionInfoDTOList = ConsumptionConverter.toComsumptionInfoDTOList(
            byMemberAndDateRange);
        return comsumptionInfoDTOList;
    }

    @Transactional
    public ConsumptionResponse.ConsumptionInfoDTOList createConsumptionInfo(
        Member member,
        ConsumptionRequest.ConsumptionCreateDTO consumptionCreateDTO,
        Category category
    ) {
        MapEntity mapEntityByPoint = findOrCreateMapEntity(consumptionCreateDTO);

        Consumption consumption = ConsumptionConverter.toConsumption(member, mapEntityByPoint,
            category,
            consumptionCreateDTO);

        consumptionRepository.save(consumption);

        LocalDate today = LocalDate.now();             // 오늘 날짜
        LocalDate firstDayOfMonth = today.withDayOfMonth(1); // 이번 달의 1일

        List<Consumption> byMemberAndDateRange = consumptionRepository.findByMemberAndDateRange(
            member, firstDayOfMonth.getMonthValue(),  // startMonth
            firstDayOfMonth.getDayOfMonth(),  // startDay (항상 1일)
            today.getMonthValue(),            // endMonth
            today.getDayOfMonth());            // endDay

        return ConsumptionConverter.toComsumptionInfoDTOList(
            byMemberAndDateRange);
    }

    /**
     * 위도, 경도를 이용해 MapEntity를 찾거나 없으면 새로 저장하는 메서드
     */
    private MapEntity findOrCreateMapEntity(ConsumptionRequest.ConsumptionCreateDTO dto) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(dto.getLng(), dto.getLat()));
        point.setSRID(4326); // SRID 설정

        return mapRepository.findMapByPoint(point.toText()) // WKT 형식
            .orElseGet(() -> mapRepository.save(MapConveter.toMapEntity(point, dto.getLocationName())));
    }

}
