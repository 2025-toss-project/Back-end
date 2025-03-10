package payroad.domain.analytics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import payroad.domain.analytics.dto.AnalyticsResponse;
import payroad.domain.analytics.dto.AnalyticsResponse.AnalyticsInfoListDTO;
import payroad.domain.analytics.service.AnalyticsService;
import payroad.domain.budget.dto.BudgetResponse;
import payroad.domain.member.Member;
import payroad.global.response.ApiResponse;
import payroad.global.security.annotation.LoginMember;

@RequiredArgsConstructor
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Operation(summary = "통계 차트 데이터 조회 Api", description = "통계차트 데이터 조회 api입니다.<br>**반환 형식(리스트)**<br>")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", description = "성공",
        content = @Content(schema = @Schema(
            implementation = AnalyticsResponse.AnalyticsInfoListDTO.class,
            type = "array",  // List 또는 array로 응답을 나타냄
            description = "통계 정보 리스트"))
    )
    @GetMapping
    public ApiResponse<List<AnalyticsResponse.AnalyticsInfoListDTO>> analyticsInfo(
        @LoginMember Member member
    ){
        List<AnalyticsInfoListDTO> analyticsInfo = analyticsService.getAnalyticsInfo(member);
        return ApiResponse.onSuccess(analyticsInfo);
    }
}
