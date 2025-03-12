package payroad.domain.consumption.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import payroad.domain.category.Category;
import payroad.domain.consumption.Consumption;
import payroad.domain.consumption.dto.ConsumptionResponse.CalenderInfoDTO;
import payroad.domain.consumption.dto.ConsumptionResponse.CalenderInfoListDTO;
import payroad.domain.consumption.dto.ConsumptionResponse.ConsumptionInfoByDateDTO;
import payroad.domain.consumption.dto.ConsumptionResponse.ConsumptionInfoDTO;
import payroad.domain.consumption.dto.ConsumptionResponse.ConsumptionInfoDTOList;
import payroad.domain.map.MapEntity;
import payroad.domain.member.Member;

public abstract class ConsumptionConverter {

    public static Consumption toConsumption(
        Member member,
        MapEntity mapEntity,
        Category category,
        ConsumptionRequest.ConsumptionCreateDTO consumptionCreateDTO
    ) {
        return Consumption.builder()
            .price(consumptionCreateDTO.getPrice())
            .details(consumptionCreateDTO.getDetail())
            .category(category)
            .date(consumptionCreateDTO.getDate())
            .member(member)
            .mapEntity(mapEntity)
            .build();
    }

    public static Consumption toUpdateConsumption(
        Category category,
        Member member,
        MapEntity mapEntity,
        ConsumptionRequest.ConsumptionUpdateDTO consumptionUpdateDTO
    ) {
        return Consumption.builder()
            .id(consumptionUpdateDTO.getId())
            .member(member)
            .category(category)
            .details(consumptionUpdateDTO.getDetail())
            .date(consumptionUpdateDTO.getDate())
            .price(consumptionUpdateDTO.getPrice())
            .mapEntity(mapEntity)
            .build();
    }

    public static ConsumptionResponse.CalenderInfoListDTO toCalenderInfoListDTO(
        List<Consumption> consumptionList
    ) {
        int totalPrice = consumptionList.stream()
            .mapToInt(Consumption::getPrice)
            .sum();

        Map<LocalDate, Integer> datePriceMap = consumptionList.stream()
            .collect(Collectors.groupingBy(
                Consumption::getDate,
                Collectors.summingInt(Consumption::getPrice)
            ));

        List<CalenderInfoDTO> calenderList = datePriceMap.entrySet().stream()
            .map(entry -> CalenderInfoDTO.builder()
                .year(entry.getKey().getYear())
                .month(entry.getKey().getMonthValue())
                .day(entry.getKey().getDayOfMonth())
                .datePrice(entry.getValue())
                .build())
            .collect(Collectors.toList());

        return CalenderInfoListDTO.builder()
            .calenderInfoDTOS(calenderList)
            .totalPrice(totalPrice)
            .build();
    }

    public static ConsumptionInfoDTOList toComsumptionInfoDTOList(
        List<Consumption> consumptionList
    ) {
        int totalPrice = consumptionList.stream()
            .mapToInt(Consumption::getPrice)
            .sum();

        // 날짜별 그룹화 (월 → 일 → 소비 내역)
        Map<Integer,Map<Integer, Map<Integer, List<ConsumptionInfoDTO>>>> groupedByDate = consumptionList.stream()
            .collect(Collectors.groupingBy(
                consumption -> consumption.getDate().getYear(),
                Collectors.groupingBy(
                    consumption -> consumption.getDate().getMonthValue(), // 월 기준 그룹화
                    Collectors.groupingBy(
                        consumption -> consumption.getDate().getDayOfMonth(), // 일 기준 그룹화
                        Collectors.mapping(
                            consumption -> ConsumptionInfoDTO.builder()
                                .id(consumption.getId())
                                .price(consumption.getPrice())
                                .category(consumption.getCategory().getName()) // 카테고리 이름
                                .details(consumption.getDetails())
                                .lat(consumption.getMapEntity().getLocation().getY()) //위도
                                .lng(consumption.getMapEntity().getLocation().getX()) // 경도
                                .locationName(consumption.getMapEntity().getName()) // 포인트 이름
                                .build(),
                            Collectors.toList()
                        )
                    )
                )
            ));

        // 그룹화된 데이터를 ConsumptionInfoByDateDTO 리스트로 변환
        List<ConsumptionInfoByDateDTO> consumptionInfoByDateDTOS = groupedByDate.entrySet().stream()
            .flatMap(yearEntry -> yearEntry.getValue().entrySet().stream()
                .flatMap(monthEntry -> monthEntry.getValue().entrySet().stream()
                    .map(dayEntry -> ConsumptionInfoByDateDTO.builder()
                        .year(yearEntry.getKey()) // 년도
                        .month(monthEntry.getKey()) // 월
                        .day(dayEntry.getKey()) // 일
                        .datePrice(dayEntry.getValue().stream().mapToInt(ConsumptionInfoDTO::getPrice)
                            .sum()) // 하루 소비 총합
                        .consumptionInfoList(dayEntry.getValue()) // 해당 날짜의 소비 내역
                        .build()
                    )
                )
            ).collect(Collectors.toList());


        // 최종 DTO 생성
        return ConsumptionInfoDTOList.builder()
            .totalPrice(totalPrice) // 전체 소비 총합
            .consumptionInfoByDateDTOS(consumptionInfoByDateDTOS) // 날짜별 소비 내역 리스트
            .build();
    }

}
