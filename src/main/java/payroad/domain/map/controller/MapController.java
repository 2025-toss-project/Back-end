package payroad.domain.map.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import payroad.domain.map.dto.MapResponse;
import payroad.domain.map.dto.MapResponse.MapDetailInfo;
import payroad.domain.map.service.MapService;
import payroad.domain.member.Member;
import payroad.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {

    private final MapService mapService;

    @GetMapping("/detail")
    public ApiResponse<MapResponse.MapDetailInfo> getMapDetailInfo(
        @RequestParam Long id
    ){
        MapDetailInfo mapDetailInfo = mapService.getMapDetailInfo(id);
        return  ApiResponse.onSuccess(mapDetailInfo);
    }
}
