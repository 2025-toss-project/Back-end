package payroad.domain.analytics.dto;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import payroad.domain.analytics.dto.AnalyticsResponse.AnalyticsInfoDTO;
import payroad.domain.analytics.dto.AnalyticsResponse.AnalyticsInfoListDTO;
import payroad.domain.consumption.Consumption;
@Slf4j
public abstract class AnalyticsConverter {

    public static List<AnalyticsInfoListDTO> toAnalyticsInfoList(
        Map<String, Map<String, Integer>> totalPrice,
        int maxTotal
    ) {

        return totalPrice.entrySet().stream()
            .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
            .map(stringIntegerMap -> {
                // 내부 맵을 List<AnalyticsInfoDTO>로 변환
                List<AnalyticsInfoDTO> analyticsInfoDTOList = stringIntegerMap.getValue().entrySet()
                    .stream()
                    .map(categoryEntry -> AnalyticsInfoDTO.builder()
                        .category(categoryEntry.getKey())  // 카테고리 이름 설정
                        .percentage((categoryEntry.getValue() * 100.0) / maxTotal)  // 비율 설정
                        .price(categoryEntry.getValue()) // 가격 설정
                        .date(stringIntegerMap.getKey())
                        .build())
                    .collect(Collectors.toList());

                // List<AnalyticsInfoDTO>로 구성된 AnalyticsInfoListDTO 객체 생성
                return AnalyticsInfoListDTO.builder()
                    .analyicsInfoDTOS(analyticsInfoDTOList)
                    .build();
            }).toList();
    }
}