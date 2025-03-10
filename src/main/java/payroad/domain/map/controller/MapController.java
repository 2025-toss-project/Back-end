package payroad.domain.map.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import payroad.domain.map.dto.MapResponse;
import payroad.domain.map.dto.MapResponse.CategoryMapInfoListDTO;
import payroad.domain.map.dto.MapResponse.MapDetailInfoDTO;
import payroad.domain.map.service.MapService;
import payroad.domain.member.Member;
import payroad.global.response.ApiResponse;
import payroad.global.security.annotation.LoginMember;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {

    private final MapService mapService;

    @Operation(summary = "단건 지출내역 조회 api", description = "지출내역 상세내역 조회하는 api입니다.<br>**반환 형식(리스트)**<br>")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", description = "성공",
        content = @Content(schema = @Schema(implementation = MapResponse.MapDetailInfoDTO.class))
    )
    @GetMapping("/detail")
    public ApiResponse<MapResponse.MapDetailInfoDTO> getMapDetailInfo(
        @RequestParam Long id
    ) {
        MapDetailInfoDTO mapDetailInfo = mapService.getMapDetailInfo(id);
        return ApiResponse.onSuccess(mapDetailInfo);
    }


    @Operation(summary = "내 반경 지출내역 조회 api", description = "내 반경에 지출내역을 조회하는 api입니다.<br>**반환 형식(리스트)**<br>")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", description = "성공",
        content = @Content(schema = @Schema(implementation = MapResponse.CategoryMapInfoListDTO.class))
    )
    @GetMapping("/all")
    public ApiResponse<MapResponse.CategoryMapInfoListDTO> getMapInfo(
        @LoginMember Member member,
        @RequestParam Double lan,
        @RequestParam Double lon,
        @RequestParam Double radius
    ) {
        // todo: 일단 50KM로 설정해두고 진행한다. 5000.0

        CategoryMapInfoListDTO allMapInfo = mapService.getAllMapInfo(member, lan, lon, 5.0);
        return ApiResponse.onSuccess(allMapInfo);
    }

    @Operation(summary = "상대방 반경 지출내역 조회 api", description = "반경내 상대방의 (카테고리 및 소비타입)지출내역을 조회하는 api입니다.<br>**반환 형식(리스트)**<br>")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", description = "성공",
        content = @Content(schema = @Schema(implementation = MapResponse.CategoryMapInfoListDTO.class))
    )
    @GetMapping("/other")
    public ApiResponse<MapResponse.CategoryMapInfoListDTO> getOtherMapInfo(
        @LoginMember Member member,
        @RequestParam String type,
        @RequestParam Double lat,
        @RequestParam Double lng,
        @RequestParam Double radius
    ){
        // todo : 일다 radius의 값은 50km로 해둔다.
        radius=50.0;

        CategoryMapInfoListDTO otherMapInfo = mapService.getOtherMapInfo(member, type, lat, lng, radius);
        return ApiResponse.onSuccess(otherMapInfo);
    }
}
