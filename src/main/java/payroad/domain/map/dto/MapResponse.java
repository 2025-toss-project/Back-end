package payroad.domain.map.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class MapResponse {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class MapDetailInfoDTO {

        private Long id;
        private String category;
        private String details;
        private String locationName;
        private Double lat;
        private Double lng;
        private int price;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class MapInfoListDTO {

        private String category;
        private List<MapDetailInfoDTO> mapInfoDTOList;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class CategoryMapInfoListDTO {

        private List<MapInfoListDTO> mapInfoListDTOList;
    }
}
