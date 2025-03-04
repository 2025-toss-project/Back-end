package payroad.domain.consumption.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class ConsumptionRequest {


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ConsumptionCreateDTO {

        int price;
        String detail;
        String category;
        Double lat;// 위도
        Double lng; // 경도
        String locationName;
        LocalDate date;
    }
}
