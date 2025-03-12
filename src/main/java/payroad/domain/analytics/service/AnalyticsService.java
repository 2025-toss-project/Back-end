package payroad.domain.analytics.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payroad.domain.analytics.dto.AnalyticsConverter;
import payroad.domain.analytics.dto.AnalyticsResponse;
import payroad.domain.consumption.Consumption;
import payroad.domain.consumption.repository.ConsumptionRepository;
import payroad.domain.member.Member;
import payroad.global.response.exception.GeneralException;
import payroad.global.response.status.ErrorStatus;
import payroad.global.util.AnalyticsUtils;
import payroad.global.util.CommonUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final ConsumptionRepository consumptionRepository;

    public List<AnalyticsResponse.AnalyticsInfoListDTO> getAnalyticsInfo(
        Member member
    ) {
        LocalDate endDate = CommonUtils.getCurrentNow();
        LocalDate startDate = CommonUtils.getMinusDate();

        List<Consumption> Consumptions = consumptionRepository.findByMemberAndDateRange(member,
            startDate, endDate);

        Map<String, Map<String, Integer>> map = AnalyticsUtils.calCategoryMap(
            Consumptions);
        int totalPrice = AnalyticsUtils.calMaxTotal(map);
        if (totalPrice == 0) {
            throw new GeneralException(ErrorStatus.CHART_NOT_FIND);
        }
        return AnalyticsConverter.toAnalyticsInfoList(map, totalPrice);
    }
}
