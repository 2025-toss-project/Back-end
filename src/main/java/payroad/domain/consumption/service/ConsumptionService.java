package payroad.domain.consumption.service;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import payroad.domain.consumption.Consumption;
import payroad.domain.consumption.dto.ConsumptionConverter;
import payroad.domain.consumption.dto.ConsumptionResponse;
import payroad.domain.consumption.dto.ConsumptionResponse.ConsumptionInfoDTOList;
import payroad.domain.consumption.repository.ConsumptionRepository;
import payroad.domain.member.Member;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsumptionService {

    private final ConsumptionRepository consumptionRepository;

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

}
