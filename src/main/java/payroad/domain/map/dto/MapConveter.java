package payroad.domain.map.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import payroad.domain.consumption.Consumption;
import payroad.domain.map.MapEntity;
import payroad.domain.map.dto.MapResponse.CategoryMapOtherInfoListDTO;
import payroad.domain.map.dto.MapResponse.MapDetailInfoDTO;
import payroad.domain.map.dto.MapResponse.MapOtherInfoDTO;
import payroad.domain.map.dto.MapResponse.MapOtherInfoListDTO;
import payroad.domain.member.AgeGroup;

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
            .date(consumption.getDate())
            .build();
    }

    public static MapResponse.CategoryMapInfoListDTO toMapInfoList(
        List<Consumption> consumptions
    ) {
        // 카테고리별로 소비 내역을 그룹화
        List<MapResponse.MapDetailInfoDTO> allDetails = consumptions.stream()
            .map(MapConveter::toMapDetailInfo)
            .collect(Collectors.toList());

        List<MapResponse.MapInfoListDTO> mapInfoListDTOList = new ArrayList<>();

        // lat, lng 별로 묶고, 같은 위치에 있는 항목들은 날짜순으로 정렬
        Map<String, MapResponse.MapLocationInfoDTO> locationInfoMap = new LinkedHashMap<>();

        for (MapResponse.MapDetailInfoDTO detailInfo : allDetails) {
            // lat, lng를 키로 묶기 위한 문자열
            String locationKey = detailInfo.getLat() + "," + detailInfo.getLng();

            // 위치가 이미 존재하면, 해당 위치에 카테고리와 날짜 정보 추가
            MapResponse.MapLocationInfoDTO locationInfo = locationInfoMap.get(locationKey);
            if (locationInfo == null) {
                locationInfo = MapResponse.MapLocationInfoDTO.builder()
                    .lat(detailInfo.getLat())
                    .lng(detailInfo.getLng())
                    .details(new ArrayList<>())
                    .totalPrice(0)
                    .build();
                locationInfoMap.put(locationKey, locationInfo);
            }

            // 날짜가 빠른 항목을 우선으로 리스트에 추가
            locationInfo.getDetails().add(detailInfo);
            locationInfo.setTotalPrice(
                locationInfo.getTotalPrice() + detailInfo.getPrice()); // 총 가격 업데이트
        }

        // MapLocationInfoDTO 리스트로 변환 후, 날짜순으로 정렬
        List<MapResponse.MapLocationInfoDTO> locationInfoList = new ArrayList<>(
            locationInfoMap.values());

        // 각 위치의 세부 정보를 날짜순으로 정렬
        locationInfoList.forEach(locationInfo -> locationInfo.getDetails()
            .sort(Comparator.comparing(MapResponse.MapDetailInfoDTO::getDate).reversed()));

        // 카테고리별로 묶은 결과 생성
        // 카테고리별로 분리하고 그 안에서 묶인 locationInfo를 추가
        Map<String, List<MapResponse.MapLocationInfoDTO>> groupedByCategory = new LinkedHashMap<>();

        for (MapResponse.MapLocationInfoDTO locationInfo : locationInfoList) {
            MapResponse.MapDetailInfoDTO firstDetailInfo = locationInfo.getDetails().get(0);
            String category = firstDetailInfo.getCategory();

            // 카테고리별로 묶어서 추가
            groupedByCategory
                .computeIfAbsent(category, k -> new ArrayList<>())
                .add(locationInfo);
        }

        // 카테고리별로 MapInfoListDTO 생성
        for (Map.Entry<String, List<MapResponse.MapLocationInfoDTO>> entry : groupedByCategory.entrySet()) {
            String category = entry.getKey();
            List<MapResponse.MapLocationInfoDTO> locationInfoForCategory = entry.getValue();

            MapResponse.MapInfoListDTO mapInfoListDTO = MapResponse.MapInfoListDTO.builder()
                .category(category)
                .mapInfoDTOList(locationInfoForCategory)
                .build();

            mapInfoListDTOList.add(mapInfoListDTO);
        }

        return MapResponse.CategoryMapInfoListDTO.builder()
            .mapInfoListDTOList(mapInfoListDTOList)
            .build();

        // 카테고리별로 MapDetailInfoDTO 리스트 생성
//        List<MapResponse.MapInfoListDTO> mapInfoListDTOList = new ArrayList<>();
//
//        // 카테고리별 MapDetailInfoDTO 리스트 생성
//        for (Map.Entry<String, List<MapResponse.MapDetailInfoDTO>> entry : groupedByCategory.entrySet()) {
//            String category = entry.getKey();
//            List<MapResponse.MapDetailInfoDTO> categoryDetailInfoList = entry.getValue();
//
//            // MapInfoListDTO 생성
//            MapResponse.MapInfoListDTO mapInfoListDTO = MapResponse.MapInfoListDTO.builder()
//                .category(category)
//                .mapInfoDTOList(categoryDetailInfoList)
//                .build();
//
//            mapInfoListDTOList.add(mapInfoListDTO);
//        }
    }

    // Consumption을 MapDetailInfoDTO로 변환
    public static MapResponse.MapOtherInfoDTO toOtherMapDetailInfo(
        Consumption consumption,
        String type
    ) {
        return MapResponse.MapOtherInfoDTO.builder()
            .category(consumption.getCategory().getName())  // Map의 카테고리
            .locationName(consumption.getMapEntity().getName())  // Map의 위치 이름
            .lat(consumption.getMapEntity().getLocation().getY())  // Map의 위도
            .lng(consumption.getMapEntity().getLocation().getX())  // Map의 경도
            .price(consumption.getPrice())  // Consumption의 가격
            .type(type)
            .ageGroup(consumption.getMember().getAgeGroup().getLabel())
            .build();
    }

    public static MapResponse.CategoryMapOtherInfoListDTO toMapOtherInfoList(
        List<Consumption> consumptions, String type
    ) {
        // 카테고리별로 소비 내역을 그룹화
        Map<String, MapOtherInfoDTO> uniqueMapInfo = consumptions.stream()
            .map(consumption -> MapConveter.toOtherMapDetailInfo(consumption, type)
            ).collect(Collectors.toMap(
                dto -> dto.getLat() + "," + dto.getLng(), // 키: "lat,lng" 문자열
                dto -> dto, // 값: dto 객체
                (existing, replacement) -> existing // 중복 발생 시 기존 값 유지
            ));

        // 카테고리별로 MapDetailInfoDTO 리스트 생성
        Map<String, List<MapResponse.MapOtherInfoDTO>> groupedByCategory = uniqueMapInfo.values()
            .stream()
            .collect(Collectors.groupingBy(MapResponse.MapOtherInfoDTO::getCategory));

        // 카테고리별 MapDetailInfoDTO 리스트 생성
        List<MapOtherInfoListDTO> list = groupedByCategory.entrySet().stream()
            .map(entry -> MapOtherInfoListDTO.builder()
                .category(entry.getKey())
                .mapInfoDTOList(entry.getValue())
                .build())
            .collect(Collectors.toList());

        return CategoryMapOtherInfoListDTO.builder()
            .mapInfoListDTOList(list)
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