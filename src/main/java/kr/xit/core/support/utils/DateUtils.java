package kr.xit.core.support.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * description : Date uitility
 *               자바 8부터 java.time 패키지 활용
 *               localDate : 날짜 정보만 필요할때
 *               localDateTime : 날짜와 시간 모두 필요할때
 *               localtime : 시간 정보만 필요할때
 * packageName : kr.xit.core.support.utils
 * fileName    : Checks
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    private static final String DEFAULT_YMD_DT_FMT ="yyyy-MM-dd HH:mm:ss";

    public static java.util.Date toDateFromLocalDate(final LocalDate localDate){
        final Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
        //return java.sql.Date.valueOf(localDate);
    }

    public static java.util.Date toDateFromLocalDateTime(final LocalDateTime localDateTime){
        final Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
        //return java.sql.Timestamp.valueOf(localDateTime);
    }

//    public static LocalDate toLocalDateFromDate(Date date){
//        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
//        //return new java.sql.Date(date.getTime()).toLocalDate();
//    }

    public static LocalDateTime toLocalDateTimeFromDate(final Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        //return new java.sql.Timestamp(date.getTime()).toLocalDateTime();
    }

    /**
     * LocalDate 객체 반환
     *
     * @return LocalDate
     */
    public static LocalDate localDate() {
        return LocalDate.now();
    }

    /**
     * LocalDate.parse 객체 반환
     *
     * @param target yyyy-MM-dd
     * @return LocalDate.parse(tartget);
     */
    public static LocalDate parseLocalDate(final String target) {
        return LocalDate.parse(target);
    }

    /**
     * LocalDateTime 객체 반환
     *
     * @return LocalDateTime
     */
    public static LocalDateTime localDateTime() {
        return LocalDateTime.now();
    }

    /**
     * LocalDateTime.parse 객체 반환
     *
     * @param target yyyy-MM-dd HH:mm:ss
     * @return LocalDateTime.parse(tartget);
     */
    public static LocalDateTime parseLocalDateTime(final String target) {
        return LocalDateTime.parse(target);
    }

    /**
     * LocalDateTime.parse 객체 반환
     *
     * @param target yyyy-MM-dd HH:mm:ss
     * @return LocalDateTime.parse(tartget);
     */
    public static LocalDateTime parseLocalDateTimeForm(final String target, final String pattern) {
        return LocalDateTime.parse(target, DateTimeFormatter.ofPattern(Checks.checkVal(pattern, DEFAULT_YMD_DT_FMT)));
    }

    /**
     * String format -> long
     * @param target 20230610115959
     * @param fmt yyyyMMddHHmmss
     * @return long long seconds
     */
    public static long parseStringToLong(final String target, final String fmt) {
        final String format = Checks.checkVal(fmt, DEFAULT_YMD_DT_FMT);
        final LocalDateTime ldt = LocalDateTime.parse(target, DateTimeFormatter.ofPattern(format));
        return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()/1000;
    }

    /**
     * LocalDateTime -> long
     * @param ldt LocalDateTime
     * @return long long seconds
     */
    public static long parseToLong(final LocalDateTime ldt) {
        return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()/1000;
    }

    /**
     * Date -> long
     * @param date Date
     * @return long long seconds
     */
    public static long parseToLong(final Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()/1000;
    }

    /**
     * 현재 년도
     *
     * @return int
     */
    public static int getCurrentYear() {
        return localDate().getYear();
    }

    /**
     * 현재 월
     *
     * @return int
     */
    public static int getCurrentMonth() {
        return localDate().getMonthValue();
    }

    /**
     * 현재 일
     *
     * @return int
     */
    public static int getCurrentDay() {
        return localDate().getDayOfMonth();
    }

    /**
     * 운년 여부
     *
     * @return boolean
     */
    public static boolean isLeapYear() {
        return localDate().isLeapYear();
    }

    /**
     * 오늘 날짜 : 년-월-일
     * BASIC_ISO_DATE : 20200621 리턴
     *
     * @return LocalDate
     */
    public static LocalDate getToday() {
        return localDate();
    }

    /**
     * 오늘 날짜 : 년-월-일
     * BASIC_ISO_DATE : 20200621 리턴
     *
     * @param delimiter 년원일 사이 구분자
     * @return String
     */
    public static String getToday(String delimiter) {
        final String pattern = "yyyy" + delimiter + "MM" + delimiter + "dd";
        String result = "";

        try {
            if (delimiter == null) {
                result = localDate().format(DateTimeFormatter.BASIC_ISO_DATE);
            } else {
                result = localDate().format(DateTimeFormatter.ofPattern(pattern));
            }
        } catch (final Exception e) {
            log.error("DateUtils::getToday", e);
        }

        return result;
    }

    /**
     * 오늘 날짜 : 년-월-일 시분초
     *
     * @return LocalDate
     */
    public static LocalDateTime getTodayAndNowTime() {
        return localDateTime();
    }

    /**
     * 현재시간 -> long return
     *
     * @return long
     */
    public static long getLongTodayAndNowTime() {
        return localDateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()/1000;
    }

    /**
     * 오늘 날짜 : 년-월-일 시분초
     *
     * @param delimiter     년원일 사이 구분자
     * @param isMillisecond 밀리 세컨드 여부
     * @return String
     */
    public static String getTodayAndNowTime(final String delimiter, final boolean isMillisecond) {
        String pattern = "";

        try {
            pattern = "yyyy" + delimiter + "MM" + delimiter + "dd" + " HH:mm:ss" + (isMillisecond ? ".SSS" : "");
        } catch (Exception e) {
            log.error("DateUtils::getTodayAndNowTime", e);
        }

        return localDateTime().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 오늘 날짜 : 년-월-일 시분초
     *
     * @param userFormat 사용자가 원하는 포멧
     * @return String
     */
    public static String getTodayAndNowTime(final String userFormat) {
        String pattern = "";

        try {
            pattern = DEFAULT_YMD_DT_FMT;
            if (!StringUtils.isEmpty(userFormat)) {
                pattern = userFormat;
            }
        } catch (Exception e) {
            log.error("DateUtils::getTodayAndNowTime", e);
        }

        return localDateTime().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 년 월 일 계산
     * 날짜만 계산 년-월-일 형태로 넘겨줘야함(안그럼 예외)
     * 해당월에 존재하지 않는 일을 넘겨도 예외
     * 빼기는 음수를 붙여서 넘기면 된다.
     * ex : -1 = 빼기 1
     *
     * @param targetDate 계산할 년-월-일
     * @param year       계산할 년
     * @param month      계산할 월
     * @param day        계산할 일
     * @return String
     */
    public static String getCalculatorDate(final String targetDate, final int year, final int month, final int day) {
        String result = "";

        try {
            LocalDate localDate = parseLocalDate(targetDate);
            localDate = localDate.plusYears(year);
            localDate = localDate.plusMonths(month);
            localDate = localDate.plusDays(day);
            result = localDate.toString();
        } catch (Exception e) {
            log.error("DateUtils::getCalculatorDate", e);
        }

        return result;
    }

    /**
     * 년 월 계산
     * 년-월-일 형태로 넘겨줘야함(안그럼 예외)
     * 해당월에 존재하지 않는 일을 넘겨도 예외
     * 빼기는 음수를 붙여서 넘기면 된다.
     * ex : -1 = 빼기 1
     *
     * @param targetDate 계산할 년-월-일
     * @param year       계산할 년
     * @param month      계산할 월
     * @return String
     */
    public static String getCalculatorDate(String targetDate, int year, int month) {
        return getCalculatorDate(targetDate, year, month, 0);
    }

    /**
     * 년 계산
     * 년-월-일 형태로 넘겨줘야함(안그럼 예외)
     * 해당월에 존재하지 않는 일을 넘겨도 예외
     * 빼기는 음수를 붙여서 넘기면 된다.
     * ex : -1 = 빼기 1
     *
     * @param targetDate 계산할 년-월-일
     * @param year       계산할 년
     * @return String
     */
    public static String getCalculatorDate(final String targetDate, final int year) {
        return getCalculatorDate(targetDate, year, 0, 0);
    }

    /**
     * 날짜&시간 계산 년-월-일THH:mm:ss 형태(안그럼 예외)
     * 2020-06-22T23:20:32 ISO 시간 표기법에 따라 'T'를 꼭 붙여야 함.
     * 빼기는 음수를 붙여서 ex : -1 = 빼기 1
     *
     * @param targetDate 계산할 년-월-일T시:분:초
     * @param year       계산할 년
     * @param month      계산할 월
     * @param day        계산할 일
     * @param hour       계산할 시간
     * @param minute     계산할 분
     * @param second     계산할 초
     * @return String
     */
    public static String getCalculatorDateAndTime(final String targetDate, final int year, final int month, final int day, int hour, int minute, int second) {
        String result = "";

        try {
            LocalDateTime localDateTime = parseLocalDateTime(targetDate);
            localDateTime = localDateTime.plusYears(year);
            localDateTime = localDateTime.plusMonths(month);
            localDateTime = localDateTime.plusDays(day);
            localDateTime = localDateTime.plusHours(hour);
            localDateTime = localDateTime.plusMinutes(minute);
            localDateTime = localDateTime.plusSeconds(second);
            result = localDateTime.toString();
        } catch (Exception e) {
            log.error("DateUtils::getCalculatorDateAndTime", e);
        }

        return result;
    }

    /**
     * 년 월 일 시 분 계산
     * 날짜&시간 계산 년-월-일THH:mm:ss 형태(안그럼 예외)
     * 2020-06-22T23:20:32 ISO 시간 표기법에 따라 'T'를 꼭 붙여야 함.
     * 빼기는 음수를 붙여서 ex : -1 = 빼기 1
     *
     * @param targetDate 계산할 년-월-일T시:분:초
     * @param year       계산할 년
     * @param month      계산할 월
     * @param day        계산할 일
     * @param hour       계산할 시간
     * @param minute     계산할 분
     * @return String
     */
    public static String getCalculatorDateAndTime(final String targetDate, final int year, final int month, final int day, final int hour, final int minute) {
        return getCalculatorDateAndTime(targetDate, year, month, day, hour, minute, 0);
    }

    /**
     * 년 월 일 시 계산
     * 2020-06-22T23:20:32 ISO 시간 표기법에 따라 'T'를 꼭 붙여야 함.
     * 빼기는 음수를 붙여서 ex : -1 = 빼기 1
     *
     * @param targetDate 계산할 년-월-일T시:분:초
     * @param year       계산할 년
     * @param month      계산할 월
     * @param day        계산할 일
     * @param hour       계산할 시간
     * @return String
     */
    public static String getCalculatorDateAndTime(final String targetDate, final int year, final int month, final int day, final int hour) {
        return getCalculatorDateAndTime(targetDate, year, month, day, hour, 0, 0);
    }

    /**
     * 년 월 일 계산
     * 2020-06-22T23:20:32 ISO 시간 표기법에 따라 'T'를 꼭 붙여야 함.
     * 빼기는 음수를 붙여서 ex : -1 = 빼기 1
     *
     * @param targetDate 계산할 년-월-일T시:분:초
     * @param year       계산할 년
     * @param month      계산할 월
     * @param day        계산할 일
     * @return String
     */
    public static String getCalculatorDateAndTime(final String targetDate, final int year, final int month, final int day) {
        return getCalculatorDateAndTime(targetDate, year, month, day, 0, 0, 0);
    }

    /**
     * 년 월 계산
     * 2020-06-22T23:20:32 ISO 시간 표기법에 따라 'T'를 꼭 붙여야 함.
     * 빼기는 음수를 붙여서 ex : -1 = 빼기 1
     *
     * @param targetDate 계산할 년-월-일T시:분:초
     * @param year       계산할 년
     * @param month      계산할 월
     * @return String
     */
    public static String getCalculatorDateAndTime(final String targetDate, final int year, final int month) {
        return getCalculatorDateAndTime(targetDate, year, month, 0, 0, 0, 0);
    }

    /**
     * 년 계산
     * 2020-06-22T23:20:32 ISO 시간 표기법에 따라 'T'를 꼭 붙여야 함.
     * 빼기는 음수를 붙여서 ex : -1 = 빼기 1
     *
     * @param targetDate 계산할 년-월-일T시:분:초
     * @param year       계산할 년
     * @return String
     */
    public static String getCalculatorDateAndTime(final String targetDate, final int year) {
        return getCalculatorDateAndTime(targetDate, year, 0, 0, 0, 0, 0);
    }

    /**
     * 2020-06-22
     * 이전 날짜 확인
     * sourceDate < compareDate = true
     * sourceDate > compareDate = false
     * sourceDate == compareDate = false
     *
     * @param sourceDate  시작 년-월-일
     * @param compareDate 비교 년-월-일
     * @return boolean
     */
    public static boolean isBeforeLocalDate(final String sourceDate, final String compareDate) {
        boolean result = false;

        try {
            LocalDate source = parseLocalDate(sourceDate);
            result = source.isBefore(parseLocalDate(compareDate));
        } catch (Exception e) {
            log.error("DateUtils::isBeforeLocalDate", e);
        }

        return result;
    }

    /**
     * 2020-06-22
     * 지난 날짜 확인
     * sourceDate > compareDate = true
     * sourceDate < compareDate = false
     * sourceDate == compareDate = false
     *
     * @param sourceDate  시작 년-월-일
     * @param compareDate 비교 년-월-일
     * @return boolean
     */
    public static boolean isAfterLocalDate(final String sourceDate, final String compareDate) {
        boolean result = false;

        try {
            LocalDate source = parseLocalDate(sourceDate);
            result = source.isAfter(parseLocalDate(compareDate));
        } catch (Exception e) {
            log.error("DateUtils::isAfterLocalDate", e);
        }

        return result;
    }

    /**
     * 2020-06-22T22:57:33
     * 이전 날짜&시간 확인
     * sourceDate < compareDate = true
     * sourceDate > compareDate = false
     * sourceDate == compareDate = false
     *
     * @param sourceDate  시작 년월일
     * @param compareDate 비교 년월일
     * @return boolean
     */
    public static boolean isBeforeLocalDateTime(final String sourceDate, final String compareDate) {
        boolean result = false;

        try {
            LocalDateTime source = parseLocalDateTime(sourceDate);
            result = source.isBefore(parseLocalDateTime(compareDate));
        } catch (final Exception e) {
            log.error("DateUtils::isBeforeLocalDateTime", e);
        }

        return result;
    }

    /**
     * 20230620235733 (Default: 2023-06-20 23:57:33)
     * pattern: yyyyMMddHHmmss (Default: yyyy-MM-dd HH:mm:ss)
     * 이전 날짜&시간 확인
     * sourceDate < compareDate = true
     * sourceDate > compareDate = false
     * sourceDate == compareDate = false
     *
     * @param sourceDate  시작 년월일
     * @param compareDate 비교 년월일
     * @param pattern 년월일 패턴
     * @return boolean
     */
    public static boolean isBeforeLocalDateTime(final String sourceDate, final String compareDate, final String pattern) {
        boolean result = false;

        try {
            LocalDateTime source = parseLocalDateTimeForm(sourceDate,pattern);
            result = source.isBefore(parseLocalDateTimeForm(compareDate,pattern));
        } catch (final Exception e) {
            log.error("DateUtils::isBeforeLocalDateTime", e);
        }

        return result;
    }

    /**
     * 2020-06-22T22:57:33
     * 지난 날짜&시간 확인
     * sourceDate > compareDate = true
     * sourceDate < compareDate = false
     * sourceDate == compareDate = false
     *
     * @param sourceDate  시작 년월일
     * @param compareDate 비교 년월일
     * @return boolean
     */
    public static boolean isAfterLocalDateTime(final String sourceDate, final String compareDate) {
        boolean result = false;

        try {
            LocalDateTime source = parseLocalDateTime(sourceDate);
            result = source.isAfter(parseLocalDateTime(compareDate));
        } catch (final Exception e) {
            log.error("DateUtils::isAfterLocalDateTime", e);
        }

        return result;
    }

    /**
     * 날짜 차이 계산
     * 2020-06-23 - 2020-06-22 = 1
     * 2020-06-23 - 2020-06-24 = -1
     * 앞에꺼 - 뒤에꺼
     *
     * @param startDate String
     * @param endDate String
     * @return int
     */
    public static int getDateDiffPeriodForLocalDate(final String startDate, final String endDate) {
        int result = 0;

        try {
            LocalDate source = parseLocalDate(endDate);
            result = (int) ChronoUnit.DAYS.between(source, parseLocalDate(startDate));
        } catch (final Exception e) {
            log.error("DateUtils::getDateDiffPeriodForLocalDate", e);
        }

        return result;
    }

    /**
     * 날짜 차이 계산(시분초까지 계산해줌)
     * 2020-06-23T23:12:45 - 2020-06-22T23:12:45 = 1
     * 2020-07-23T23:59:59 - 2020-07-24T23:59:58 = 0
     * 2020-07-23T23:59:58 - 2020-07-24T23:59:59 = -1
     * 앞에꺼 - 뒤에꺼
     *
     * @param startDate String
     * @param endDate String
     * @return int
     */
    public static int getDateDiffPeriodForLocalDateTime(final String startDate, final String endDate) {
        int result = 0;

        try {
            LocalDateTime source = parseLocalDateTime(endDate);
            result = (int) ChronoUnit.DAYS.between(source, parseLocalDateTime(startDate));
        } catch (final Exception e) {
            log.error("DateUtils::getDateDiffPeriodForLocalDateTime", e);
        }

        return result;
    }

    /**
     *
     * @param date
     * @param sDateFormat
     * @return
     */
    public static String getFormatedDT(final Date date, final String sDateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(sDateFormat);
        return sdf.format(date);
    }
}
