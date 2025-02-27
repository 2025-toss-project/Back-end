package payroad.global.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BudgetUtils {

    public static int calculateToPercentage(int totalPrice, int price) {
        return totalPrice / price * 100;
    }

    public static Map<String, Integer> getSumByCategory(
        List<Object[]> lists
    ) {
        return lists.stream()
            .collect(Collectors.toMap(
                list -> String.valueOf(list[0]),
                list -> ((Number) list[1]).intValue(),
                Integer::sum
            ));
    }
}
