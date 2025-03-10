package payroad.domain.analytics.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AnalyticsResponse {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AnalyticsInfoDTO {
        String category;
        int price;
        double percentage;
        String date; // 년도와 달만
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AnalyticsInfoListDTO{
        List<AnalyticsInfoDTO> analyicsInfoDTOS;
    }
}
