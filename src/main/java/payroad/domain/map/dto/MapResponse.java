package payroad.domain.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class MapResponse {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class MapDetailInfo{
        private Long id;
        private String category;
        private String details;
        private String locationName;
    }
}
