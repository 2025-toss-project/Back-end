package payroad.domain.consumption.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class ConsumptionResponse {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class ConsumptionInfoDTO {

        private long id;
        private int price;
        private String category;
        private String details;
        private Double lat;
        private Double lng;
        private String localName;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class ConsumptionInfoByDateDTO {
        private int year;
        private int month;
        private int day;
        private int datePrice;
        private List<ConsumptionInfoDTO> consumptionInfoList;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class ConsumptionInfoDTOList {
        private int totalPrice;
        private List<ConsumptionInfoByDateDTO> consumptionInfoByDateDTOS;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class CalenderInfoDTO{
        private int year;
        private int month;
        private int day;
        private int datePrice;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class CalenderInfoListDTO{
        private int totalPrice;
        List<CalenderInfoDTO> calenderInfoDTOS;
    }
}
