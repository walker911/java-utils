package com.walker.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * java 8 日期
 *
 * @author walker
 * @date 2019/3/27
 */
public class Date8Util {


    private static final String NORMAL_DATE_PATTERN = "yyyy-MM-dd";
    private static final String PURE_YEAR_MONTH_PATTERN = "yyyyMM";

    private Date8Util() {
    }

    /**
     * Date to LocalDate
     *
     * @param date 日期
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date to LocalDateTime
     *
     * @param date 日期
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.of("GMT+8")).toLocalDateTime();
    }

    /**
     * LocalDate to Date
     *
     * @param date LocalDate
     * @return Date
     */
    public static Date toDate(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime to Date
     *
     * @param dateTime LocalDateTime
     * @return Date
     */
    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.of("GMT+8")).toInstant());
    }

    /**
     * Date to Instant
     *
     * @param date 日期
     * @return Instant
     */
    public static Instant toInstant(Date date) {
        return Instant.ofEpochMilli(date.getTime());
    }

    /**
     * Instant to Date
     *
     * @param instant 时刻
     * @return 日期
     */
    public static Date toDate(Instant instant) {
        return new Date(instant.toEpochMilli());
    }

    public static Instant toBeijingInstant(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+08:00"));
    }

    public static String format(Date date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(toLocalDateTime(date));
    }

    public static Date parse(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return toDate(LocalDateTime.parse(date, formatter));
    }

    /**
     * 获取某天开始时间00:00的时间戳
     *
     * @param date
     * @return
     */
    public static long startTimestampOfDay(LocalDate date) {
        LocalDateTime startTime = LocalDateTime.of(date, LocalTime.MIN);
        return startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static long endTimestampOfDay(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MAX).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static Date startDateOfDay(LocalDate date) {
        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.MIN);
        ZonedDateTime zonedDateTime = startDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date endDateOfDay(LocalDate date) {
        return Date.from(LocalDateTime.of(date, LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 当月第一天
     *
     * @param date 日期
     * @return
     */
    public static String firstDayOfMonth(LocalDate date) {
        return DateTimeFormatter.ofPattern(NORMAL_DATE_PATTERN).format(date.with(TemporalAdjusters.firstDayOfMonth()));
    }

    /**
     * 当月最后一天
     *
     * @param date 日期
     * @return
     */
    public static String lastDayOfMonth(LocalDate date) {
        return DateTimeFormatter.ofPattern(NORMAL_DATE_PATTERN).format(date.with(TemporalAdjusters.lastDayOfMonth()));
    }

    /**
     * 转换成年月格式
     *
     * @param date
     * @return
     */
    public static String parseYearMonth(LocalDate date) {
        return DateTimeFormatter.ofPattern(PURE_YEAR_MONTH_PATTERN).format(date);
    }

    public static boolean isMatch(LocalDate date) {
        LocalDate now = LocalDate.now();

        MonthDay birthday = MonthDay.of(date.getMonth(), date.getDayOfMonth());
        MonthDay currentMonthDay = MonthDay.from(now);

        return currentMonthDay.equals(birthday);
    }


    public static void main(String[] args) {
    }
}
