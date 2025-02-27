package payroad.domain.budget.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class BudgetRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class BudgetCreateDTO {

        @NotNull
        int price;
        @NotNull
        String category;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class BudgetCreateListDTO {

        int totalPrice;
        List<BudgetCreateDTO> budgetCreateDTOList;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class BudgetUpdateDTO {

        @NotNull
        Long budgetId;
        @NotNull
        int price;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class BudgetUpdateListDTO {

        List<BudgetUpdateDTO> budgetUpdateDTOList;
    }
}
