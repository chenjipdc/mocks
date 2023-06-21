package top.chenjipdc.mocks.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    private static final String DEFAULT_TIMEZONE_ID = ZoneId.systemDefault()
            .getId();
    private static final String DEFAULT_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_ERROR_MSG = "date range error, start date must be smaller or equal to end date.";

    private static final String DATE_ERROR_USAGE_MSG = "date error, usage: ";

    // ------------------- random return date   -------------------

    public static Date random() {
        return random(new Date());
    }

    public static Date random(Date maxDate) {
        return random(maxDate.getTime());
    }

    public static Date random(long maxTime) {
        return randomRangeDate(0L,
                maxTime);
    }

    public static Date random(String maxDate) {
        return random(maxDate,
                DEFAULT_FORMAT_STRING);
    }

    public static Date random(String maxDate, String format) {
        return random(maxDate,
                format,
                DEFAULT_TIMEZONE_ID);
    }

    public static Date random(String maxDate, String format, String timeZoneId) {
        final SimpleDateFormat simpleDateFormat = dateFormat(format,
                timeZoneId);
        try {
            return random(simpleDateFormat.parse(maxDate)
                    .getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException(DATE_ERROR_USAGE_MSG + DEFAULT_FORMAT_STRING);
        }
    }

    // ------------------- range return date   -------------------

    public static Date randomRangeDate(long start, long end) {
        if (start > end) {
            throw new IllegalArgumentException(DATE_ERROR_MSG);
        }
        return new Date(start + (NumericUtils.nextLong(end - start + 1)));
    }

    public static Date randomRangeDate(String startDate, String endDate) {
        return randomRangeDate(startDate,
                endDate,
                DEFAULT_FORMAT_STRING);
    }

    public static Date randomRangeDate(String startDate, String endDate, String format) {
        return randomRangeDate(startDate,
                endDate,
                format,
                DEFAULT_TIMEZONE_ID);
    }

    public static Date randomRangeDate(String startDate, String endDate, String format, String timeZoneId) {
        try {
            final SimpleDateFormat simpleDateFormat = dateFormat(format,
                    timeZoneId);
            final long start = simpleDateFormat
                    .parse(startDate)
                    .getTime();
            final long end = simpleDateFormat
                    .parse(endDate)
                    .getTime();
            if (start > end) {
                throw new IllegalArgumentException(DATE_ERROR_MSG);
            }
            return randomRangeDate(start,
                    end);
        } catch (ParseException e) {
            throw new IllegalArgumentException(DATE_ERROR_USAGE_MSG + DEFAULT_FORMAT_STRING);
        }
    }

    // ------------------- random return string   -------------------


    public static String randomString() {
        return randomString(new Date());
    }

    public static String randomString(Date maxDate) {
        return randomString(maxDate,
                DEFAULT_FORMAT_STRING);
    }

    public static String randomString(Date maxDate, String format) {
        return randomString(maxDate,
                format,
                DEFAULT_TIMEZONE_ID);
    }

    public static String randomString(Date maxDate, String format, String timeZoneId) {
        return randomString(maxDate.getTime(),
                format,
                timeZoneId);
    }

    public static String randomString(String maxDate) {
        try {
            return randomString(defaultDateFormat()
                    .parse(maxDate)
                    .getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException(DATE_ERROR_USAGE_MSG + DEFAULT_FORMAT_STRING);
        }
    }

    public static String randomString(long maxTime) {
        return randomString(maxTime,
                DEFAULT_FORMAT_STRING);
    }

    public static String randomString(long maxTime, String format) {
        return randomString(maxTime,
                format,
                DEFAULT_TIMEZONE_ID);
    }

    public static String randomString(long maxTime, String format, String timeZoneId) {
        return randomStringRangeDate(0,
                maxTime,
                format,
                timeZoneId);
    }

    // ------------------- format return string   -------------------

    public static String randomStringFormat(String format) {
        return randomStringFormat(format,
                DEFAULT_TIMEZONE_ID);
    }

    public static String randomStringTimeZone(String timeZoneId) {
        return randomStringFormat(DEFAULT_FORMAT_STRING,
                timeZoneId);
    }

    public static String randomStringFormat(String format, String timeZoneId) {
        return randomString(randomInterval(),
                format,
                timeZoneId);
    }

    // ------------------- range return string   -------------------

    public static String randomStringRangeDate(long start, long end) {
        return randomStringRangeDate(start,
                end,
                DEFAULT_FORMAT_STRING);
    }

    public static String randomStringRangeDate(long start, long end, String format) {
        return randomStringRangeDate(start,
                end,
                format,
                DEFAULT_TIMEZONE_ID);
    }

    public static String randomStringRangeDate(long start, long end, String format, String timeZoneId) {
        if (start > end) {
            throw new IllegalArgumentException(DATE_ERROR_MSG);
        }
        final SimpleDateFormat simpleDateFormat = dateFormat(format,
                timeZoneId);
        return simpleDateFormat.format(start + (NumericUtils.nextLong(end - start + 1)));
    }

    public static String randomStringRangeDate(String startDate, String endDate) {
        return randomStringRangeDate(startDate,
                endDate,
                DEFAULT_FORMAT_STRING);
    }

    public static String randomStringRangeDate(String startDate, String endDate, String format) {
        return randomStringRangeDate(startDate,
                endDate,
                format,
                DEFAULT_TIMEZONE_ID);
    }

    public static String randomStringRangeDate(String startDate, String endDate, String format, String timeZoneId) {
        try {
            final SimpleDateFormat dateFormat = dateFormat(format,
                    timeZoneId);
            final long start = dateFormat
                    .parse(startDate)
                    .getTime();
            final long end = dateFormat
                    .parse(endDate)
                    .getTime();
            if (start > end) {
                throw new IllegalArgumentException(DATE_ERROR_MSG);
            }
            return randomStringRangeDate(start,
                    end,
                    format,
                    timeZoneId);
        } catch (ParseException e) {
            throw new IllegalArgumentException(DATE_ERROR_USAGE_MSG + DEFAULT_FORMAT_STRING);
        }
    }

    // -------------------  other   -------------------

    public static long randomInterval() {
        return randomInterval(System.currentTimeMillis());
    }

    public static long randomInterval(long maxTime) {
        return NumericUtils.nextLong(maxTime + 1);
    }

    public static SimpleDateFormat defaultDateFormat() {
        return dateFormat(DEFAULT_FORMAT_STRING,
                DEFAULT_TIMEZONE_ID);
    }

    public static SimpleDateFormat dateFormat(String format) {
        return dateFormat(format,
                DEFAULT_TIMEZONE_ID);
    }

    private static SimpleDateFormat dateFormat(String format, String timeZoneId) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        return simpleDateFormat;
    }
}
