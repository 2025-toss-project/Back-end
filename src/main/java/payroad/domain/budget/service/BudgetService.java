package payroad.domain.budget.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payroad.domain.budget.Budget;
import payroad.domain.budget.dto.BudgetConverter;
import payroad.domain.budget.dto.BudgetRequest;
import payroad.domain.budget.dto.BudgetResponse;
import payroad.domain.budget.dto.BudgetResponse.BudgetInfoListDTO;
import payroad.domain.budget.repository.BudgetRepository;
import payroad.domain.category.Category;
import payroad.domain.category.repository.CategoryRepository;
import payroad.domain.consumption.repository.ConsumptionRepository;
import payroad.domain.member.Member;
import payroad.global.response.exception.GeneralException;
import payroad.global.response.status.ErrorStatus;
import payroad.global.util.BudgetUtils;
import payroad.global.util.CommonUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final ConsumptionRepository consumptionRepository;
    private final CategoryRepository categoryRepository;

    public BudgetResponse.BudgetInfoListDTO getBudgetInfoList(Member member) {
        List<Budget> budgets = budgetRepository.findByMemberId(member)
            .orElseThrow(() -> new GeneralException(ErrorStatus.BUDGET_EMPTY));

        List<Object[]> consumptionByCategory = consumptionRepository.findConsumptionByMemberAndMonth(
            member, CommonUtils.getCurrentMonth(), CommonUtils.getCurrentYear());

        Map<String, Integer> sumByCategory = BudgetUtils.getSumByCategory(consumptionByCategory);

        return BudgetConverter.toBudgetInfoList(budgets, sumByCategory);
    }

    @Transactional
    public BudgetResponse.BudgetInfoListDTO createBudgetInfo(
        Member member,
        BudgetRequest.BudgetCreateListDTO budgetCreateListDTO
    ) {
        List<Budget> budgetList = budgetCreateListDTO.getBudgetCreateDTOList().stream()
            .map(budget -> {
                Category category = categoryRepository.findByName(budget.getCategory())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.CATEGORY_NOT_FIND));
                return
                    BudgetConverter.toBudget(budget, member, category);
            }).toList();

        budgetRepository.saveAll(budgetList);

        return getBudgetInfoList(member);
    }

    @Transactional
    public BudgetResponse.BudgetInfoListDTO updateBudgetInfo(
        Member member,
        BudgetRequest.BudgetUpdateListDTO budgetUpdateListDTO
    ) {
        budgetUpdateListDTO.getBudgetUpdateDTOList().forEach(dto -> {
            Budget budget = budgetRepository.findById(dto.getBudgetId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.BUDGET_NOT_FIND));

            budget.setPrice(dto.getPrice());
        });

        return getBudgetInfoList(member);
    }
}
