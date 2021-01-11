package org.example.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Description: 日期工具类
 * Author: houry
 * Date: 2020/10/27 10:48
 * Version: V1.0
 */
public class DateUtils {

    public enum DateType {
        NORM_DATE_PATTERN("yyyy-MM-dd"),
        NORM_DATETIME_PATTERN("yyyy-MM-dd HH:mm:ss"),
        NORM_TIME_PATTERN("HH:mm:ss"),
        NORM_DATETIME_MINUTE_PATTERN("yyyy-MM-dd HH:mm"),
        NORM_DATETIME_MS_PATTERN("yyyy-MM-dd HH:mm:ss.SSS"),
        CHINESE_DATE_PATTERN("yyyy年MM月dd日"),
        PURE_DATE_PATTERN("yyyyMMdd"),
        PURE_TIME_PATTERN("HHmmss"),
        PURE_DATETIME_PATTERN("yyyyMMddHHmmss"),
        PURE_DATETIME_MS_PATTERN("yyyyMMddHHmmssSSS"),
        PURE_DATE_MD_PATTERN("MMdd"),
        ;
        private String format;

        DateType(String format) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }
    }

    /**
     * 定义一周的每天表示的常量，用于返回中文展示
     */
    private static final String[] DAY_OF_WEEK = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    /**
     * 把长整型的时间戳转换成日期
     *
     * @param time   时间戳
     * @param format 格式化日期格式
     * @return 格式化之后的日期
     */
    public static String stampToDate(long time, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return dateTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
    }

    /**
     * 把日期转换成指定格式的字符串日期
     *
     * @param date 传递的日期
     * @param type 指定格式
     * @return 字符串日期
     */
    public static String dateToString(Date date, DateType type) {
        ZonedDateTime now = dateToZonedDateTime(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(type.getFormat());
        return now.format(formatter);
    }

    /**
     * 把日期转换成字默认的符串日期
     *
     * @param date 传递日期
     * @return 字符串日期
     */
    public static String dateToString(Date date) {
        return dateToString(date, DateType.NORM_DATETIME_MINUTE_PATTERN);
    }

    /**
     * 获取当前年份和月份，按照指定的分隔符拼接 2021-03
     *
     * @param separator 分隔符
     * @return 日期字符串
     */
    public static String getCurrentYearAndMonth(String separator) {
        String month = LocalDate.now().getMonth().getValue() < 10 ? "0" + LocalDate.now().getMonth().getValue() : "" + LocalDate.now().getMonth().getValue();
        return LocalDate.now().getYear() + separator + month;
    }

    /**
     * 获取当前年份的一月 按照指定的分隔符拼接：2021-01
     *
     * @param separator 分隔符
     * @return 日期字符串
     */
    public static String getCurrentYearWithFirstMonth(String separator) {
        return LocalDate.now().getYear() + separator + "01";
    }


    /**
     * 把当前日期格式化成指定的的字符串日期
     *
     * @param type 指定的格式化日期
     * @return 字符串日期
     */
    public static String dateToString(DateType type) {
        return dateToString(new Date(), type);
    }

    /**
     * 把当前日期格式化成默认的字符串日期
     *
     * @return 字符串日期
     */
    public static String dateToString() {
        return dateToString(new Date(), DateType.NORM_DATETIME_MINUTE_PATTERN);
    }

    /**
     * 将字符串日期转换成Date
     *
     * @param dateStr  日期字符串，可以是 20200101、2020-01-01、2020年01月01日等格式
     * @param dateType 格式化日期类型，必须包含完整的年月日
     * @return Date类型日期
     */
    public static Date strToDate(String dateStr, DateType dateType) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType.getFormat());
        return Date.from(LocalDate.parse(dateStr, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant());

    }

    /**
     * 将字符串日期转换成Date
     *
     * @param dateStr  日期字符串，可以是 "20200101131313"或者"2020-01-01 13:13:13"格式
     * @param dateType 格式化日期类型，必须包含完整的年月日时分秒
     * @return Date类型日期
     */
    public static Date strToDateTime(String dateStr, DateType dateType) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType.getFormat());
        return Date.from(LocalDateTime.parse(dateStr, formatter).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 判断是否是当天
     *
     * @param date 传递的时间
     * @return true|false
     */
    public static boolean isToday(Date date) {
        return LocalDate.now().equals(dateToLocalDate(date));
    }


    /**
     * 获取给定日期之前或者之后的日期
     * beforeOrAfterNumDay(new Date(), 0) 当天 2020-10-10
     * beforeOrAfterNumDay(new Date(), -1) 当天的前一天 2020-10-09
     * beforeOrAfterNumDay(new Date(), 1) 当天的后一天  2020-10-11
     *
     * @param date 给定的Date类型的日期
     * @param num  天数偏移量
     * @return 计算之后的日期
     */
    public static Date getBeforeOrAfterNumDay(Date date, long num) {
        return localDateToDate(dateToLocalDate(date).plusDays(num));
    }

    /**
     * 获取当前日期之前或者之后的月份
     * beforeOrAfterNumMonth(0) 当月 2020-10
     * beforeOrAfterNumMonth(-1) 当天的前一月 2020-09
     * beforeOrAfterNumMonth(1) 当天的后一月 2020-11
     *
     * @param num 月份偏移量
     * @return 计算之后的月份
     */
    public static String getBeforeOrAfterNumMonth(int num) {
        String date = LocalDate.now().plusMonths(num).toString();
        return date.substring(0, date.lastIndexOf("-"));
    }

    /**
     * 给当前时间增加年数
     *
     * @param date 当前时间
     * @param num  增加的数量
     * @return 返回增加日期之后的新日期
     */
    public static Date addYear(Date date, int num) {
        return localDateTimeToDate(dateToLocalDateTime(date).plusYears(num));
    }

    /**
     * 给当前时间增加月数
     *
     * @param date 当前时间
     * @param num  增加的数量
     * @return 返回增加日期之后的新日期
     */
    public static Date addMonth(Date date, int num) {
        return localDateTimeToDate(dateToLocalDateTime(date).plusMonths(num));
    }

    /**
     * 给当前时间增加天数
     *
     * @param date 当前时间
     * @param num  增加的数量
     * @return 返回增加日期之后的新日期
     */
    public static Date addDay(Date date, int num) {
        return localDateTimeToDate(dateToLocalDateTime(date).plusDays(num));
    }

    /**
     * 给当前时间增加小时
     *
     * @param date 当前时间
     * @param num  增加的数量
     * @return 返回增加日期之后的新日期
     */
    public static Date addHours(Date date, int num) {
        return localDateTimeToDate(dateToLocalDateTime(date).plusHours(num));
    }

    /**
     * 给当前时间增加分钟
     *
     * @param date 当前时间
     * @param num  增加的数量
     * @return 返回增加日期之后的新日期
     */
    public static Date addMinutes(Date date, int num) {
        return localDateTimeToDate(dateToLocalDateTime(date).plusMinutes(num));
    }

    /**
     * 给当前时间增加秒数
     *
     * @param date 当前时间
     * @param num  增加的数量
     * @return 返回增加日期之后的新日期
     */
    public static Date addSeconds(Date date, int num) {
        return localDateTimeToDate(dateToLocalDateTime(date).plusSeconds(num));
    }

    /**
     * 给当前时间增加纳秒
     *
     * @param date 当前时间
     * @param num  增加的数量
     * @return 返回增加日期之后的新日期
     */
    public static Date addNanos(Date date, int num) {
        return localDateTimeToDate(dateToLocalDateTime(date).plusNanos(num));
    }

    /**
     * 给当前时间增加周数
     *
     * @param date 当前时间
     * @param num  增加的数量
     * @return 返回增加日期之后的新日期
     */
    public static Date addWeeks(Date date, int num) {
        return localDateTimeToDate(dateToLocalDateTime(date).plusWeeks(num));
    }


    /**
     * 获取当天是星期几
     *
     * @return 整形的星期数
     */
    public static int getDayOfWeek() {
        return LocalDate.now().atStartOfDay(ZoneId.systemDefault()).getDayOfWeek().getValue();
    }

    /**
     * 获取当天是星期几
     *
     * @return 中文表示的星期数
     */
    public static String getDayOfWeekWithChinese() {
        return DAY_OF_WEEK[getDayOfWeek() - 1];
    }

    /**
     * 获取传递的日期在当天是星期几
     *
     * @param date 传递的日期
     * @return 整形的星期数
     */
    public static int getDayOfWeek(Date date) {
        return dateToLocalDate(date).atStartOfDay(ZoneId.systemDefault()).getDayOfWeek().getValue();
    }

    /**
     * 获取传递的日期在当天是星期几
     *
     * @param date 传递的日期
     * @return 中文表示的星期数
     */
    public static String getDayOfWeekWithChinese(Date date) {
        return DAY_OF_WEEK[getDayOfWeek(date) - 1];
    }

    /**
     * 判断给定的日期是否在给定的日期范围内
     *
     * @param date      给定的日期
     * @param beginTime 开始日期
     * @param endTime   结束日期
     * @return true|false
     */
    public static boolean belongDateRange(Date date, Date beginTime, Date endTime) {
        return date.before(beginTime) && date.after(endTime);
    }

    /**
     * 获取当前的小时（24小时制）
     *
     * @return 当前的小时
     */
    public static int getNowHour() {
        return LocalDateTime.now().getHour();
    }

    /**
     * 根据指定日期获取年龄
     *
     * @param birthday 指定日期
     * @return 年龄
     */
    public static int getAgeByBirthday(Date birthday) {
        LocalDateTime birthdayDate = dateToLocalDateTime(birthday);
        if (LocalDateTime.now().isBefore(birthdayDate)) {
            return 0;
        }
        Duration duration = Duration.between(birthdayDate, LocalDateTime.now());
        return (int) duration.toDays();
    }

    /**
     * 根据传入的时间获取这个时间是第几周
     *
     * @param date 指定的时间
     * @return 周数
     */
    public static int getWeekOfYear(Date date) {
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
        return dateToLocalDateTime(date).get(weekFields.weekOfYear());
    }

    /**
     * 获取给定时间的上个小时的开始时间和结束时间
     * 给一个为"2020-10-26-09:36:24"得到结果为 [2020-10-26 08:00:00, 2020-10-26 08:59:59, 08]
     *
     * @param date 给定的时间
     * @return 字符串数组
     */
    public static String[] getPreviousBeginHourAndEndHour(Date date) {
        String[] lastHours = new String[3];
        String hour = dateToLocalDateTime(date).getHour() - 1 + "";
        if (hour.length() < 2) {
            hour = 0 + hour;
        }
        LocalDate localDateTime = dateToLocalDate(date);
        lastHours[0] = localDateTime + " " + hour + ":00:00";
        lastHours[1] = localDateTime + " " + hour + ":59:59";
        lastHours[2] = hour;
        return lastHours;
    }


    /**
     * 获取上周的开始日期和结束日期
     *
     * @return 字符串数组
     */
    public static String[] getPreviousBeginDayAndEndDay() {
        return getPreviousBeginDayAndEndDay(new Date());
    }

    /**
     * 根据给定的时间获取上周的开始日期和结束日期
     *
     * @param date 给定的时间
     * @return 字符串数组
     */
    public static String[] getPreviousBeginDayAndEndDay(Date date) {
        LocalDate prevDay = dateToLocalDate(date).plusWeeks(-1);
        DayOfWeek week = prevDay.getDayOfWeek();
        int value = week.getValue();
        return new String[]{prevDay.minusDays(value - 1).toString(), prevDay.plusDays(7 - value).toString()};
    }

    /**
     * 根据传入的开始日期和结束日期获取两者之间的日期集合，格式为 “yyyy-MM-dd”
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 日期集合
     */
    public static List<String> getDatesBetweenStartDateAndEndDate(String start, String end) {
        List<String> list = new ArrayList<>(10);
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 1) {
            return list;
        }
        Stream.iterate(startDate, d -> d.plusDays(1)).limit(distance + 1).forEach(f -> list.add(f.toString()));
        return list;
    }

    /**
     * 根据传入的开始日期和结束日期获取两者之间的日期集合
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 日期集合
     */
    public static List<Date> getDatesBetweenStartDateAndEndDate(Date start, Date end) {
        List<Date> list = new ArrayList<>(10);
        LocalDate startDate = dateToLocalDate(start);
        LocalDate endDate = dateToLocalDate(end);
        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 1) {
            return list;
        }
        Stream.iterate(startDate, d -> d.plusDays(1)).limit(distance + 1).forEach(f -> list.add(localDateToDate(f)));
        return list;
    }

    /**
     * 获取给定日期的月份的最后一天
     *
     * @param yearMonth 给定的月份，格式为“yyyy-MM” 2020-01---->2020-02-31
     * @return 本月最后一天
     */
    public static String getLastDayOfMonth(String yearMonth) {
        LocalDate resDate = LocalDate.parse(yearMonth + "-01");
        Month month = resDate.getMonth();
        int length = month.length(resDate.isLeapYear());
        return LocalDate.of(resDate.getYear(), month, length).toString();

    }

    /**
     * 根据传入的秒数获取格式化之后的时间
     * getFormatBySeconds(799) ---> 00:13:19
     *
     * @param seconds 给定的秒数
     * @return 格式化之后的时间字符串
     */
    public static String getTimeBySeconds(int seconds) {
        if (seconds > 60 * 60 * 24 - 1) {
            return "23:59:59";
        }
        seconds = seconds % 86400;
        String hours = String.valueOf(seconds / 3600).length() > 1 ? String.valueOf(seconds / 3600) : "0" + (seconds / 3600);
        seconds = seconds % 3600;
        String minutes = String.valueOf(seconds / 60).length() > 1 ? String.valueOf(seconds / 60) : "0" + (seconds / 60);
        String second = String.valueOf(seconds % 60).length() > 1 ? String.valueOf(seconds % 60) : "0" + (seconds % 60);
        return hours + ":" + minutes + ":" + second;
    }

    /**
     * 计算给定两个日期之前的时间差
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 时间差值（单位秒）
     */
    public static int getTimeDifference(Date begin, Date end) {
        return (int) (((end.getTime() - begin.getTime()) / (1000)));
    }

    /**
     * 获取给定时间的日期集合，包含开始时间和结束时间
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 时间范围集合
     */
    public static List<String> getRangeOfDates(String beginTime, String endTime) {
        List<String> dayList = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(beginTime);
        LocalDate endDate = LocalDate.parse(endTime);
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        LongStream.range(0, days + 1).forEach(d -> dayList.add(startDate.plusDays(d).toString()));
        return dayList;
    }


    /**
     * 把Date类型的日期转化成ZoneDateTime类型的日期
     *
     * @param date date类型日期
     * @return ZoneDateTime类型日期
     */
    private static ZonedDateTime dateToZonedDateTime(Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 把ZoneDateTime类型的日期转化成Date类型的日期
     *
     * @param zonedDateTime ZoneDateTime类型日期
     * @return date类型日期
     */
    private static Date zonedDateTimeToDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 把Date类型的日志转换成LocalDate类型的日期
     *
     * @param date Date类型日期
     * @return LocalDate类型日期
     */
    private static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 把LocalDate类型的日志转换成Date类型的日期
     *
     * @param localDate LocalDate类型日期
     * @return Date类型日期
     */
    private static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 把Date类型的日志转换成LocalDateTime类型的日期
     *
     * @param date Date类型日期
     * @return LocalDateTime类型日期
     */
    private static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 把LocalDateTime类型的日志转换成Date类型的日期
     *
     * @param localDateTime LocalDateTime类型日期
     * @return Date类型日期
     */
    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
