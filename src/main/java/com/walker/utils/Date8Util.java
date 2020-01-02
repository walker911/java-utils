package com.walker.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Locale;

/**
 * java 8
 *
 * @author walker
 * @date 2019/3/27
 */
public class Date8Util {

    /**
     * 获取当天日期
     *
     * @return
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    public static int year(LocalDate date) {
        return date.getYear();
    }

    public static int month(LocalDate date) {
        return date.getMonthValue();
    }

    public static int day(LocalDate date) {
        return date.getDayOfMonth();
    }

    public static LocalDate setDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    public static boolean compare(LocalDate date1, LocalDate date2) {
        return date1.equals(date2);
    }

    public static LocalTime time() {
        return LocalTime.now();
    }

    public static LocalTime addHour(LocalTime time, int hour) {
        return time.plusHours(hour);
    }

    public static LocalDate addWeek(LocalDate date, int week) {
        return date.plus(week, ChronoUnit.WEEKS);
    }

    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date localDateToDate(LocalDate date) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = date.atStartOfDay(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date localDateTimeToDate(LocalDateTime dateTime) {
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static Instant toInstant(Date date) {
        return Instant.ofEpochMilli(date.getTime());
    }

    public static Date toDate(Instant instant) {
        return new Date(instant.toEpochMilli());
    }

    public static Instant toBeijingInstant(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+08:00"));
    }

    public static String format(Date date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(dateToLocalDateTime(date));
    }

    public static Date parse(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTimeToDate(LocalDateTime.parse(date, formatter));
    }

    /**
     * 获取某天开始时间00:00的时间戳
     *
     * @param date
     * @return
     */
    public static long getStartTimeOfDay(LocalDate date) {
        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.MIN);
        return startDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static Date getStartDateOfDay(LocalDate date) {
        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.MIN);
        ZonedDateTime zonedDateTime = startDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    private static final String NORMAL_DATE_PATTERN = "yyyy-MM-dd";
    private static final String PURE_YEAR_MONTH_PATTERN = "yyyyMM";

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

    public static void main(String[] args) {
        String dayAfter = "20190328";
        LocalDate format = LocalDate.parse(dayAfter, DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println(format.toString());
        String goodFriday = "Jan 18 2014";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);
        LocalDate holiday = LocalDate.parse(goodFriday, formatter);
        System.out.println(holiday);
        LocalDateTime today = LocalDateTime.now();
        String formatTime = today.format(DateTimeFormatter.ofPattern("MM dd yyyy"));
        System.out.println(formatTime);
        System.out.println(dateToLocalDate(new Date()));
        System.out.println(localDateToDate(LocalDate.now()));
        System.out.println(dateToLocalDateTime(new Date()));
        System.out.println(localDateTimeToDate(LocalDateTime.now()));
        YearMonth yearMonth = YearMonth.now();
        System.out.printf("yearMonth: %s day: %d\n", yearMonth, yearMonth.lengthOfMonth());
        System.out.println(Clock.systemUTC().millis());
        System.out.println(Clock.systemDefaultZone().getZone());
        System.out.println(format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        System.out.println(parse("2019-05-20 15:51:24", "yyyy-MM-dd HH:mm:ss"));
        LocalDateTime lastAtTomorrow1 = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MAX);
        System.out.println(lastAtTomorrow1);
        LocalDateTime lastAtTomorrow2 = LocalTime.MAX.atDate(LocalDate.now().plusDays(1));
        System.out.println(lastAtTomorrow2);
        LocalDateTime lastDayAtMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
        System.out.println(lastDayAtMonth);
    }
}
