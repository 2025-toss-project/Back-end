package payroad.global.util;

import java.time.LocalDate;

public abstract class CommonUtils {

    public static int getCurrentMonth() {
        return LocalDate.now().getMonthValue();  // 현재 월을 반환 (1~12)
    }

    public static int getCurrentYear() {
        return LocalDate.now().getYear();  // 현재 년도를 반환
    }
}
