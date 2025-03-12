package payroad.domain.map.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.PrimitiveIterator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import payroad.domain.category.Category;

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
        private LocalDate date;
        private int price;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class MapLocationInfoDTO{
        private double lat;
        private double lng;
        @Setter
        private int totalPrice;
        List<MapDetailInfoDTO> details;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class MapInfoListDTO {

        private String category;
        private List<MapLocationInfoDTO> mapInfoDTOList;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class CategoryMapInfoListDTO {

        private List<MapInfoListDTO> mapInfoListDTOList;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class MapOtherInfoDTO {
        private String type;
        private String ageGroup;
        private String category;
        private String locationName;
        private Double lat;
        private Double lng;
        private int price;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class MapOtherInfoListDTO {

        private String category;
        private List<MapOtherInfoDTO> mapInfoDTOList;
    }

}
