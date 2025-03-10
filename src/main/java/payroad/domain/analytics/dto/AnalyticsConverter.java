package payroad.domain.analytics.dto;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import payroad.domain.analytics.dto.AnalyticsResponse.AnalyticsInfoDTO;
import payroad.domain.analytics.dto.AnalyticsResponse.AnalyticsInfoListDTO;
import payroad.domain.consumption.Consumption;

public abstract class AnalyticsConverter {

    public static List<AnalyticsInfoListDTO> toAnalyticsInfoList(
        Map<String, Map<String, Integer>> totalPrice,
        int maxTotal
    ) {

        return totalPrice.entrySet().stream()
            .map(stringIntegerMap -> {
                // 내부 맵을 List<AnalyticsInfoDTO>로 변환
                List<AnalyticsInfoDTO> analyticsInfoDTOList = stringIntegerMap.getValue().entrySet().stream()
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
