package payroad.domain.consumption.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import payroad.domain.consumption.Consumption;
import payroad.domain.consumption.dto.ConsumptionResponse.ConsumptionInfoByDateDTO;
import payroad.domain.consumption.dto.ConsumptionResponse.ConsumptionInfoDTO;
import payroad.domain.consumption.dto.ConsumptionResponse.ConsumptionInfoDTOList;

public abstract class ConsumptionConverter {

    public static ConsumptionResponse.ConsumptionInfoDTOList toComsumptionInfoDTOList(
        List<Consumption> consumptionList
    ) {
        int totalPrice = consumptionList.stream()
            .mapToInt(Consumption::getPrice)
            .sum();


        // 날짜별 그룹화 (월 → 일 → 소비 내역)
        Map<Integer, Map<Integer, List<ConsumptionInfoDTO>>> groupedByDate = consumptionList.stream()
            .collect(Collectors.groupingBy(
                consumption -> consumption.getDate().getMonthValue(), // 월 기준 그룹화
                Collectors.groupingBy(
                    consumption -> consumption.getDate().getDayOfMonth(), // 일 기준 그룹화
                    Collectors.mapping(
                        consumption -> ConsumptionInfoDTO.builder()
                            .id(consumption.getId())
                            .price(consumption.getPrice())
                            .category(consumption.getCategory().getName()) // 카테고리 이름
                            .details(consumption.getDetails())
                            .point(consumption.getMap().getLocation())
                            .point_name(consumption.getMap().getName()) // 포인트 이름
                            .build(),
                        Collectors.toList()
                    )
                )
            ));

        // 그룹화된 데이터를 ConsumptionInfoByDateDTO 리스트로 변환
        List<ConsumptionInfoByDateDTO> consumptionInfoByDateDTOS = groupedByDate.entrySet().stream()
            .flatMap(monthEntry -> monthEntry.getValue().entrySet().stream()
                .map(dayEntry -> ConsumptionInfoByDateDTO.builder()
                    .Month(monthEntry.getKey()) // 월
                    .Day(dayEntry.getKey()) // 일
                    .datePrice(dayEntry.getValue().stream().mapToInt(ConsumptionInfoDTO::getPrice).sum()) // 하루 소비 총합
                    .consumptionInfoList(dayEntry.getValue()) // 해당 날짜의 소비 내역
                    .build()
                )
            )
            .collect(Collectors.toList());

        // 최종 DTO 생성
        return ConsumptionInfoDTOList.builder()
            .totalPrice(totalPrice) // 전체 소비 총합
            .consumptionInfoByDateDTOS(consumptionInfoByDateDTOS) // 날짜별 소비 내역 리스트
            .build();


    }

}
