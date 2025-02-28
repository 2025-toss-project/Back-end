package payroad.domain.consumption.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import payroad.domain.consumption.dto.ConsumptionResponse;
import payroad.domain.consumption.dto.ConsumptionResponse.ConsumptionInfoDTOList;
import payroad.domain.consumption.service.ConsumptionService;
import payroad.domain.member.Member;
import payroad.global.response.ApiResponse;

@RestController
@RequestMapping("/consumption")
@RequiredArgsConstructor
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    @GetMapping
    public ApiResponse<ConsumptionResponse.ConsumptionInfoDTOList> getConsumptionInfo(
        Member member,
        @RequestParam String Category,
        @RequestParam int startMonth,
        @RequestParam int startDay,
        @RequestParam int endMonth,
        @RequestParam int endDay
    ) {
        // todo: category 별로 받아서 처리하는 로직을 추가해야함
        ConsumptionInfoDTOList consumptionInfo = consumptionService.getConsumptionInfo(member,
            startMonth, startDay, endMonth, endDay);

        return ApiResponse.onSuccess(consumptionInfo);
    }
}
