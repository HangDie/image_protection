package cn.hd.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public final class DateUtil {

    /** 线程安全的日期格式对象 */
    private static final ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>() {

        @Override
        protected DateFormat initialValue() {
            // 完整日期
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /** 线程安全的日期格式对象 */
    private static final ThreadLocal<DateFormat> YMD = new ThreadLocal<DateFormat>() {

        @Override
        protected DateFormat initialValue() {
            // 年月日
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private static final ThreadLocal<DateFormat> YEAR = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            // 年月日
            return new SimpleDateFormat("yyyy");
        }
    };

    /**
     * 格式化完整日期
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss格式的字符串
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.get().format(date);
    }

    /**
     * 格式化年月日
     *
     * @return yyyy-MM-dd 格式的字符串
     */
    public static String formatYMD(Date date) {
        return YMD.get().format(date);
    }

    /**
     * 格式化年
     *
     * @param date
     * @return yyyy字符串
     */
    public static String formatYear(Date date) {
        return YEAR.get().format(date);
    }

    /**
     * 日期
     * @param dateStr
     * @return
     */
    public static Date formatDateString(String dateStr) throws ParseException {
        return DATE_FORMAT.get().parse(dateStr);
    }


    public static String dayComparePrecise(Date fromDate,Date toDate){
        Calendar  from  =  Calendar.getInstance();
        from.setTime(fromDate);
        Calendar  to  =  Calendar.getInstance();
        to.setTime(toDate);

        int fromYear = from.get(Calendar.YEAR);
        int fromMonth = from.get(Calendar.MONTH);
        int fromDay = from.get(Calendar.DAY_OF_MONTH);

        int toYear = to.get(Calendar.YEAR);
        int toMonth = to.get(Calendar.MONTH);
        int toDay = to.get(Calendar.DAY_OF_MONTH);
        int year = toYear  -  fromYear;
        int month = toMonth  - fromMonth;
        int day = toDay  - fromDay;
        StringBuilder builder = new StringBuilder();
        if(year > 0) {
            builder.append(year).append("年");
        }
        if(month > 0) {
            builder.append(month).append("月");
        }
        if(day > 0) {
            builder.append(day).append("日");
        }
        return builder.toString();
    }

}
