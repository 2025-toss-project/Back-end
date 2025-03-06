package payroad.domain.map.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import payroad.domain.consumption.Consumption;
import payroad.domain.consumption.repository.ConsumptionRepository;
import payroad.domain.map.dto.MapConveter;
import payroad.domain.map.dto.MapResponse;
import payroad.domain.member.Member;
import payroad.domain.member.Type;
import payroad.global.response.exception.GeneralException;
import payroad.global.response.status.ErrorStatus;
import payroad.global.util.MapUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MapService {

    private final ConsumptionRepository consumptionRepository;

    public MapResponse.MapDetailInfoDTO getMapDetailInfo(
        Long budgetId
    ) {
        Consumption consumption = consumptionRepository.findById(budgetId)
            .orElseThrow(() -> new GeneralException(ErrorStatus.CONSUMPTION_NOT_FIND));

        return MapConveter.toMapDetailInfo(consumption);
    }

    public MapResponse.CategoryMapInfoListDTO getAllMapInfo(
        Member member,
        Double lat,
        Double lng,
        Double radius
    ) {
        String point = "POINT(" + lat + " " + lng + ")";
        List<Consumption> consumptions = consumptionRepository.
            findConsumptionsByMemberAndRadius(member.getId(), point, radius);

        return MapConveter.toMapInfoList(consumptions);
    }

    public MapResponse.CategoryMapInfoListDTO getOtherMapInfo(
        Member member, String type, Double lan, Double lon, Double radius
    ) {
        int typeEnum = Type.valueOf(type).ordinal();
        Long id = member.getId();
        List<Consumption> consumptions = consumptionRepository.findByNotMember(id, typeEnum);

        List<Consumption> consumptionByDistance = MapUtils.getConsumptionByDistance(consumptions,
            radius, lan, lon);

        return MapConveter.toMapInfoList(consumptionByDistance);
    }

}
