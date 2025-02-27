package payroad.domain.budget.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import payroad.domain.budget.Budget;
import payroad.domain.budget.dto.BudgetResponse.BudgetInfoDTO;
import payroad.domain.budget.dto.BudgetResponse.BudgetInfoListDTO;
import payroad.domain.category.Category;
import payroad.domain.member.Member;
import payroad.global.util.BudgetUtils;

public abstract class BudgetConverter {

    public static BudgetResponse.BudgetInfoListDTO toBudgetInfoList(
        List<Budget> budgetList,
        Map<String, Integer> ConsumptionSumByCategory
    ) {
        List<BudgetResponse.BudgetInfoDTO> budgetInfoList = budgetList.stream()
            .map(budget ->
                BudgetInfoDTO.builder()
                    .id(budget.getId())
                    .budgetPrice(budget.getPrice())
                    .category(budget.getCategory().getName())
                    .spendPrice(
                        ConsumptionSumByCategory.getOrDefault(budget.getCategory().getName(), 0))
                    .percentage(BudgetUtils.calculateToPercentage(
                        budget.getPrice(),
                        ConsumptionSumByCategory.getOrDefault(budget.getCategory().getName(), 0)))
                    .build()).collect(Collectors.toList());

        int totalBudget = budgetList.stream().mapToInt(Budget::getPrice).sum();
        int totalSpend = ConsumptionSumByCategory.values().stream().mapToInt(Integer::intValue)
            .sum();

        return BudgetResponse.BudgetInfoListDTO.builder()
            .totalBudget(totalBudget)
            .totalSpend(totalSpend)
            .totalPercentage(BudgetUtils.calculateToPercentage(totalBudget, totalSpend))
            .budgetInfoList(budgetInfoList)
            .build();
    }

    public static Budget toBudget(BudgetRequest.BudgetCreateDTO budgetCreateDTO, Member member,
        Category category) {
        return Budget.builder()
            .price(budgetCreateDTO.getPrice())
            .member(member)
            .category(category)
            .build();
    }

}
