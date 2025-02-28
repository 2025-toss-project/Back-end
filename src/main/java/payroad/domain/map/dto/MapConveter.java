package payroad.domain.map.dto;

import payroad.domain.consumption.Consumption;

public abstract class MapConveter {

    public static MapResponse.MapDetailInfo toMapDetailInfo(
        Consumption consumption
    ) {
        return MapResponse.MapDetailInfo.builder()
            .id(consumption.getId())
            .details(consumption.getDetails())
            .locationName(consumption.getMap().getName())
            .category(consumption.getCategory().getName())
            .build();
    }
}
