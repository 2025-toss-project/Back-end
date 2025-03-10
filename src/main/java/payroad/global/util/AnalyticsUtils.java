package payroad.global.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import payroad.domain.consumption.Consumption;

public abstract class AnalyticsUtils {

    public static int calMaxTotal(Map<String, Map<String, Integer>> map){
        return map.values().stream()
            .mapToInt(categoryMap -> categoryMap.values().stream()  // 각 categoryMap에서 값들을 스트림으로 변환
                .mapToInt(Integer::intValue)  // Integer를 int로 변환
                .sum())  // 합을 구합니다
            .max()   // 합들 중 최대값을 구합니다
            .orElse(0);
    }

    public static Map<String, Map<String, Integer>> calCategoryMap(List<Consumption> consumptions){
        return consumptions.stream()
            .collect(
                Collectors.groupingBy(c -> c.getDate().getYear() + "-" + String.format("%02d",
                        c.getDate().getMonthValue()),
                    Collectors.groupingBy(consumption -> consumption.getCategory().getName(),
                        Collectors.summingInt(Consumption::getPrice)
                    )
                )
            );
    }
}
