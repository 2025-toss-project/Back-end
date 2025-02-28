package payroad.domain.map.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payroad.domain.consumption.Consumption;
import payroad.domain.consumption.repository.ConsumptionRepository;
import payroad.domain.map.dto.MapConveter;
import payroad.domain.map.dto.MapResponse;
import payroad.domain.map.repository.MapRepository;
import payroad.domain.member.Member;
import payroad.global.response.ApiResponse;
import payroad.global.response.exception.GeneralException;
import payroad.global.response.status.ErrorStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MapService {

    private final ConsumptionRepository consumptionRepository;
    private final MapRepository mapRepository;

    public MapResponse.MapDetailInfo getMapDetailInfo(
        Long budgetId
    ) {
        Consumption consumption = consumptionRepository.findById(budgetId)
            .orElseThrow(() -> new GeneralException(ErrorStatus.CONSUMPTION_NOT_FIND));

        return MapConveter.toMapDetailInfo(consumption);
    }

}
