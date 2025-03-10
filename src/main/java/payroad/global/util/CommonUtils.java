package payroad.global.util;

import java.time.LocalDate;

public abstract class CommonUtils {

    public static int getCurrentMonth() {
        return LocalDate.now().getMonthValue();  // 현재 월을 반환 (1~12)
    }

    public static int getCurrentYear() {
        return LocalDate.now().getYear();  // 현재 년도를 반환
    }

    public static LocalDate getCurrentNow(){ // 현재 LocalDate값을 반환
        return LocalDate.now();
    }

    public static LocalDate getMinusDate(){ // 현재 LocalDate값을 반환
        return LocalDate.now().minusMonths(12).withDayOfMonth(1);
    }

}
