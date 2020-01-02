package com.walker.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author mu qin
 * @date 2019/12/27
 */
public class DateUtils {
    public static void main(String[] args) {
        // Instant 当前时刻
        Instant now = Instant.ofEpochMilli(System.currentTimeMillis());
        System.out.println(now);
        // LocalDateTime表示与时区无关的日期和时间
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(startOfDay(localDateTime));
        System.out.println(endOfDay(localDateTime));
    }

    public static String startOfDay(LocalDateTime ldt) {
        return ldt.with(ChronoField.MILLI_OF_DAY, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String endOfDay(LocalDateTime ldt) {
        return LocalDateTime.of(ldt.toLocalDate(), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 转换Instant为Date
     *
     * @param instant
     * @return
     */
    public static Date toDate(Instant instant) {
        return new Date(instant.toEpochMilli());
    }

    /**
     * 转换Date为Instant
     *
     * @param date
     * @return
     */
    public static Instant toInstant(Date date) {
        return Instant.ofEpochMilli(date.getTime());
    }

    /**
     * 转换LocalDateTime为北京时刻
     *
     * @param ldt
     * @return
     */
    public static Instant toBeijingInstant(LocalDateTime ldt) {
        return ldt.toInstant(ZoneOffset.of("+08:00"));
    }


}
