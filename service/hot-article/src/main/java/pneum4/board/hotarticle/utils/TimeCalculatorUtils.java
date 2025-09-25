package pneum4.board.hotarticle.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeCalculatorUtils {

    public static Duration calculateDurationToMidnight() {
        LocalDateTime now = LocalDateTime.now();
        // 오늘 날짜에 하루 더하고 시간을 00시 00분으로 만듬
        LocalDateTime midnight = now.plusDays(1).with(LocalTime.MIDNIGHT);
        return Duration.between(now, midnight);
    }
}
