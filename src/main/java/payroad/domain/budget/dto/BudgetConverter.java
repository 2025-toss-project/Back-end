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
            .filter( budget -> !"전체".equals(budget.getCategory().getName()))
            .map(budget ->
                BudgetInfoDTO.builder()
                    .id(budget.getId())
                    .budgetPrice(budget.getPrice())
                    .category(budget.getCategory().getName())
                    .spendPrice(
                        ConsumptionSumByCategory.getOrDefault(budget.getCategory().getName(), 0))
                    .percentage(BudgetUtils.calculateToPercentage(
                        ConsumptionSumByCategory.getOrDefault(budget.getCategory().getName(), 0),
                        budget.getPrice()))
                    .build()).collect(Collectors.toList());

        int totalBudget = budgetList.stream()
            .filter(budget -> "전체".equals(budget.getCategory().getName()))
            .mapToInt(Budget::getPrice).sum();
        int totalSpend = ConsumptionSumByCategory.values().stream().mapToInt(Integer::intValue)
            .sum();

        return BudgetResponse.BudgetInfoListDTO.builder()
            .totalBudget(totalBudget)
            .totalSpend(totalSpend)
            .totalPercentage(BudgetUtils.calculateToPercentage(totalBudget, totalSpend))
            .budgetInfoList(budgetInfoList)
            .build();
    }

    public static Budget toBudget(int price, Member member,
        Category category) {
        return Budget.builder()
            .price(price)
            .member(member)
            .category(category)
            .build();
    }

}
