package payroad.domain.map.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Point;
import payroad.domain.consumption.Consumption;
import payroad.domain.map.MapEntity;
import payroad.domain.map.dto.MapResponse.MapDetailInfoDTO;

public abstract class MapConveter {

    public static MapEntity toMapEntity(
        Point point,
        String name
    ) {
        return MapEntity.builder()
            .name(name)
            .location(point)
            .build();
    }

    // Consumption을 MapDetailInfoDTO로 변환
    public static MapResponse.MapDetailInfoDTO toMapDetailInfo(
        Consumption consumption
    ) {
        return MapResponse.MapDetailInfoDTO.builder()
            .id(consumption.getId())  // Map의 ID
            .category(consumption.getCategory().getName())  // Map의 카테고리
            .details(consumption.getDetails())  // Map의 세부 사항
            .locationName(consumption.getMapEntity().getName())  // Map의 위치 이름
            .lat(consumption.getMapEntity().getLocation().getY())  // Map의 위도
            .lng(consumption.getMapEntity().getLocation().getX())  // Map의 경도
            .price(consumption.getPrice())  // Consumption의 가격
            .build();
    }


    public static MapResponse.CategoryMapInfoListDTO toMapInfoList(
        List<Consumption> consumptions
    ) {
        // 카테고리별로 소비 내역을 그룹화
        Map<String, List<MapDetailInfoDTO>> groupedByCategory = consumptions.stream()
            .map(MapConveter::toMapDetailInfo)
            .collect(Collectors.groupingBy(MapResponse.MapDetailInfoDTO::getCategory));

        // 카테고리별로 MapDetailInfoDTO 리스트 생성
        List<MapResponse.MapInfoListDTO> mapInfoListDTOList = new ArrayList<>();

        // 카테고리별 MapDetailInfoDTO 리스트 생성
        for (Map.Entry<String, List<MapResponse.MapDetailInfoDTO>> entry : groupedByCategory.entrySet()) {
            String category = entry.getKey();
            List<MapResponse.MapDetailInfoDTO> categoryDetailInfoList = entry.getValue();

            // MapInfoListDTO 생성
            MapResponse.MapInfoListDTO mapInfoListDTO = MapResponse.MapInfoListDTO.builder()
                .category(category)
                .mapInfoDTOList(categoryDetailInfoList)
                .build();

            mapInfoListDTOList.add(mapInfoListDTO);
        }

        return MapResponse.CategoryMapInfoListDTO.builder()
            .mapInfoListDTOList(mapInfoListDTOList)
            .build();
    }
}

//    public static MapResponse.MapDetailInfoDTO toMapDetailInfo(
//        Consumption consumption
//    ) {
//        return MapResponse.MapDetailInfoDTO.builder()
//            .id(consumption.getId())
//            .details(consumption.getDetails())
//            .locationName(consumption.getMap().getName())
//            .category(consumption.getCategory().getName())
//            .build();
//    }