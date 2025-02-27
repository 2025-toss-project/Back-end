package payroad.domain.budget.controller;

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
import payroad.domain.member.Member;
import payroad.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    public ApiResponse<BudgetResponse.BudgetInfoListDTO> getBudget(
        Member member
    ) {
        BudgetInfoListDTO budgetInfoList = budgetService.getBudgetInfoList(member);
        return ApiResponse.onSuccess(budgetInfoList);
    }

    @PostMapping("/create")
    public ApiResponse<BudgetResponse.BudgetInfoListDTO> createBudget(
        Member member,
        @RequestBody BudgetRequest.BudgetCreateListDTO budgetCreateListDTO
    ) {
        BudgetInfoListDTO budgetInfo = budgetService.createBudgetInfo(member, budgetCreateListDTO);
        return ApiResponse.onSuccess(budgetInfo);

    }

    @PostMapping("/update")
    public ApiResponse<BudgetResponse.BudgetInfoListDTO> updateBudget(
        Member member,
        @RequestBody BudgetRequest.BudgetUpdateListDTO budgetUpdateListDTO
    ) {
        BudgetInfoListDTO budgetInfoListDTO = budgetService.updateBudgetInfo(member,
            budgetUpdateListDTO);
        return ApiResponse.onSuccess(budgetInfoListDTO);
    }


}
