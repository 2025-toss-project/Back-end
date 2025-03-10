package payroad.domain.analytics.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import payroad.domain.analytics.dto.AnalyticsResponse;
import payroad.domain.analytics.dto.AnalyticsResponse.AnalyticsInfoListDTO;
import payroad.domain.analytics.service.AnalyticsService;
import payroad.domain.member.Member;
import payroad.global.response.ApiResponse;
import payroad.global.security.annotation.LoginMember;

@RequiredArgsConstructor
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    public ApiResponse<List<AnalyticsResponse.AnalyticsInfoListDTO>> analyticsInfo(
        @LoginMember Member member
    ){
        List<AnalyticsInfoListDTO> analyticsInfo = analyticsService.getAnalyticsInfo(member);
        return ApiResponse.onSuccess(analyticsInfo);
    }
}
