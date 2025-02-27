package payroad.domain.budget.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class BudgetResponse {

    public static class BudgetCreateDTO {

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BudgetInfoDTO {

        private long id;
        private String category;
        private int budgetPrice;
        private int spendPrice;
        private int percentage;

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BudgetInfoListDTO {

        private int totalBudget;
        private int totalSpend;
        private int totalPercentage;
        private List<BudgetInfoDTO> budgetInfoList;
    }


}
