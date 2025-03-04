package payroad.domain.budget.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import payroad.domain.budget.dto.BudgetRequest;
import payroad.domain.budget.dto.BudgetResponse;
import payroad.domain.budget.dto.BudgetResponse.BudgetInfoListDTO;
import payroad.domain.budget.service.BudgetService;
import payroad.domain.consumption.dto.ConsumptionResponse;
import payroad.domain.member.Member;
import payroad.global.response.ApiResponse;
import payroad.global.security.annotation.LoginMember;

@RestController
@RequiredArgsConstructor
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    @Operation(summary = "예산 관리 금액 조회 Api", description = "내 예산관리 금액 조회 api입니다.<br>**반환 형식(리스트)**<br>")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", description = "성공",
        content = @Content(schema = @Schema(implementation = BudgetResponse.BudgetInfoListDTO.class))
    )
    @GetMapping
    public ApiResponse<BudgetResponse.BudgetInfoListDTO> getBudget(
        @LoginMember Member member
    ) {
        BudgetInfoListDTO budgetInfoList = budgetService.getBudgetInfoList(member);
        return ApiResponse.onSuccess(budgetInfoList);
    }

    @Operation(summary = "예산 관리 금액 생성 Api", description = "내 예산관리 금액 생성 api입니다.<br>**반환 형식(리스트)**<br>")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", description = "성공",
        content = @Content(schema = @Schema(implementation = BudgetResponse.BudgetInfoListDTO.class))
    )
    @PostMapping("/create")
    public ApiResponse<BudgetResponse.BudgetInfoListDTO> createBudget(
        @LoginMember Member member,
        @RequestBody BudgetRequest.BudgetCreateListDTO budgetCreateListDTO
    ) {
        BudgetInfoListDTO budgetInfo = budgetService.createBudgetInfo(member, budgetCreateListDTO);
        return ApiResponse.onSuccess(budgetInfo);

    }

    @Operation(summary = "예산 관리 금액 수정 Api", description = "내 예산관리 금액 수정 api입니다.<br>**반환 형식(리스트)**<br>")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", description = "성공",
        content = @Content(schema = @Schema(implementation = BudgetResponse.BudgetInfoListDTO.class))
    )
    @PostMapping("/update")
    public ApiResponse<BudgetResponse.BudgetInfoListDTO> updateBudget(
        @LoginMember Member member,
        @RequestBody BudgetRequest.BudgetUpdateListDTO budgetUpdateListDTO
    ) {
        BudgetInfoListDTO budgetInfoListDTO = budgetService.updateBudgetInfo(member,
            budgetUpdateListDTO);
        return ApiResponse.onSuccess(budgetInfoListDTO);
    }


}
