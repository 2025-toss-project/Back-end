package payroad.domain.consumption.service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payroad.domain.category.Category;
import payroad.domain.consumption.Consumption;
import payroad.domain.consumption.dto.ConsumptionConverter;
import payroad.domain.consumption.dto.ConsumptionRequest;
import payroad.domain.consumption.dto.ConsumptionRequest.ConsumptionUpdateDTO;
import payroad.domain.consumption.dto.ConsumptionResponse;
import payroad.domain.consumption.dto.ConsumptionResponse.ConsumptionInfoDTOList;
import payroad.domain.consumption.repository.ConsumptionRepository;
import payroad.domain.map.MapEntity;
import payroad.domain.map.dto.MapConveter;
import payroad.domain.map.repository.MapRepository;
import payroad.domain.member.Member;
import payroad.global.response.exception.GeneralException;
import payroad.global.response.status.ErrorStatus;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsumptionService {

    private final ConsumptionRepository consumptionRepository;
    private final MapRepository mapRepository;

    public ConsumptionResponse.ConsumptionInfoDTOList getConsumptionInfo(
        Member member,
        LocalDate startDate,
        LocalDate endDate
    ) {
        List<Consumption> byMemberAndDateRange = consumptionRepository.findByMemberAndDateRange(
            member, startDate, endDate);
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
        MapEntity mapEntityByPoint = findOrCreateMapEntity(consumptionCreateDTO.getLat(),
            consumptionCreateDTO.getLng(), consumptionCreateDTO.getLocationName());

        log.info("MapEntity: {}", mapEntityByPoint);
        Consumption consumption = ConsumptionConverter.toConsumption(member, mapEntityByPoint,
            category,
            consumptionCreateDTO);

        consumptionRepository.save(consumption);

        return getConsumptionInfoDTOList(member);
    }

    @Transactional
    public ConsumptionResponse.ConsumptionInfoDTOList updateConsumptionInfo(
        Member member,
        Category category,
        ConsumptionRequest.ConsumptionUpdateDTO consumptionUpdateDTO
    ) {
        Consumption exisitiongConsumption = consumptionRepository.findById(
                consumptionUpdateDTO.getId())
            .orElseThrow(() -> new GeneralException(ErrorStatus.CONSUMPTION_NOT_FIND));

        MapEntity mapEntity = findOrCreateMapEntity(consumptionUpdateDTO.getLat(),
            consumptionUpdateDTO.getLng(), consumptionUpdateDTO.getLocationName());

        exisitiongConsumption = ConsumptionConverter.toUpdateConsumption(
            category,
            member,
            mapEntity,
            consumptionUpdateDTO
        );

        consumptionRepository.save(exisitiongConsumption);

        return getConsumptionInfoDTOList(member);
    }

    @Transactional
    public ConsumptionResponse.ConsumptionInfoDTOList deleteConsumptionInfo(Member member,Long consumptionId) {
        consumptionRepository.findById(consumptionId)
            .orElseThrow(() -> new GeneralException(ErrorStatus.CONSUMPTION_NOT_FIND));
        consumptionRepository.deleteById(consumptionId);
        return getConsumptionInfoDTOList(member);
    }

    private ConsumptionInfoDTOList getConsumptionInfoDTOList(Member member) {

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
    private MapEntity findOrCreateMapEntity(Double lat, Double lng, String locationName) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(lng, lat));
        point.setSRID(4326); // SRID 설정;
        String pointText = "POINT(" + lat + " " + lng + ")";
        log.info("pointText: " + pointText);
        return mapRepository.findMapByPoint(pointText)
            .orElseGet(() -> mapRepository.save(MapConveter.toMapEntity(point, locationName)));
    }

}
