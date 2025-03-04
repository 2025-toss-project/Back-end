package payroad.domain.consumption.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import payroad.domain.category.Category;
import payroad.domain.category.service.CategoryService;
import payroad.domain.consumption.dto.ConsumptionRequest;
import payroad.domain.consumption.dto.ConsumptionResponse;
import payroad.domain.consumption.dto.ConsumptionResponse.ConsumptionInfoDTOList;
import payroad.domain.consumption.service.ConsumptionService;
import payroad.domain.map.dto.MapResponse;
import payroad.domain.member.Member;
import payroad.global.response.ApiResponse;
import payroad.global.security.annotation.LoginMember;

@RestController
@RequestMapping("/consumption")
@RequiredArgsConstructor
public class ConsumptionController {

    private final ConsumptionService consumptionService;
    private final CategoryService categoryService;

    @Operation(summary = "지출내역 조회 api", description = "내 지출내역을 카테고리와 시작 끝 날짜를 입력을 받고 조회해주는 api입니다.<br>**반환 형식(리스트)**<br>")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", description = "성공",
        content = @Content(schema = @Schema(implementation = ConsumptionResponse.ConsumptionInfoDTOList.class))
    )
    @GetMapping
    public ApiResponse<ConsumptionResponse.ConsumptionInfoDTOList> getConsumptionInfo(
        @LoginMember Member member,
        @RequestParam String category,
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

    @Operation(summary = "내 지출내역 추가 api", description = "내 지출내역을 만드는 api입니다.<br>**반환 형식(리스트)**<br>")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", description = "성공",
        content = @Content(schema = @Schema(implementation = ConsumptionResponse.ConsumptionInfoDTOList.class))
    )
    @PostMapping("/create")
    public ApiResponse<ConsumptionResponse.ConsumptionInfoDTOList> createConsumption(
        @LoginMember Member member,
        @RequestBody ConsumptionRequest.ConsumptionCreateDTO consumptionCreateDTO
    ) {
        System.out.println(consumptionCreateDTO);
        Category byName = categoryService.findByName(consumptionCreateDTO.getCategory());
        ConsumptionInfoDTOList consumptionInfo = consumptionService.createConsumptionInfo(member,
            consumptionCreateDTO, byName);
        return ApiResponse.onSuccess(consumptionInfo);
    }

    @PostMapping("/update")
    public ApiResponse<ConsumptionResponse.ConsumptionInfoDTOList> updateConsumption(
        @LoginMember Member member,
        @RequestBody ConsumptionRequest.ConsumptionUpdateDTO consumptionUpdateDTO
    ) {
        Category category = categoryService.findByName(consumptionUpdateDTO.getCategory());
        ConsumptionInfoDTOList consumptionInfoDTOList = consumptionService.updateConsumptionInfo(
            member, category, consumptionUpdateDTO);
        return ApiResponse.onSuccess(consumptionInfoDTOList);
    }
}
