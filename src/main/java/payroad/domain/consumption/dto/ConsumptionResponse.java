package payroad.domain.consumption.dto;

import jakarta.validation.constraints.NotNull;
import java.awt.Point;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
        private Point point;
        private String point_name;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class ConsumptionInfoByDateDTO {
        private int Month;
        private int Day;
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
}
