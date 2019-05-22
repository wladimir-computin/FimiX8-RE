package com.fimi.kernel.utils;

import com.fimi.kernel.R;
import com.fimi.kernel.base.BaseApplication;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    public static final String AM = "AM";
    public static final String PM = "PM";
    public static final String dateFormatHM = "HH:mm";
    public static final String dateFormatHMS = "HH:mm:ss";
    public static final String dateFormatMD = "MM/dd";
    public static final String dateFormatYM = "yyyy-MM";
    public static final String dateFormatYMD = "yyyy-MM-dd";
    public static final String dateFormatYMDHM = "yyyy-MM-dd HH:mm";
    public static final String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String dateFormatYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static Date getDateByFormat(String strDate, String format) {
        Date date = null;
        try {
            return new SimpleDateFormat(format).parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    public Date getDateByOffset(Date date, int calendarField, int offset) {
        Calendar c = new GregorianCalendar();
        try {
            c.setTime(date);
            c.add(calendarField, offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    public static String getStringByOffset(String strDate, String format, int calendarField, int offset) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(mSimpleDateFormat.parse(strDate));
            c.add(calendarField, offset);
            return mSimpleDateFormat.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return mDateTime;
        }
    }

    public static String getStringByOffset(Date date, String format, int calendarField, int offset) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(date);
            c.add(calendarField, offset);
            return mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }
    }

    public static String getStringByFormat(Date date, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        String strDate = null;
        try {
            strDate = mSimpleDateFormat.format(date);
            if (mSimpleDateFormat.format(date).toString().equals(mSimpleDateFormat.format(new Date()).toString())) {
                return BaseApplication.getContext().getString(R.string.date_today);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    public static String getStringByFormat(String strDate, String format) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            c.setTime(new SimpleDateFormat(dateFormatYMDHMS).parse(strDate));
            return new SimpleDateFormat(format).format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return mDateTime;
        }
    }

    public static String getStringByFormat(long milliseconds, String format) {
        String thisDateTime = null;
        try {
            return new SimpleDateFormat(format).format(Long.valueOf(milliseconds));
        } catch (Exception e) {
            e.printStackTrace();
            return thisDateTime;
        }
    }

    public static String getCurrentDate(String format) {
        String curDateTime = null;
        try {
            return new SimpleDateFormat(format).format(new GregorianCalendar().getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return curDateTime;
        }
    }

    public static String getCurrentDateByOffset(String format, int calendarField, int offset) {
        String mDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            c.add(calendarField, offset);
            return mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return mDateTime;
        }
    }

    public static int getOffectDay(long milliseconds1, long milliseconds2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(milliseconds1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(milliseconds2);
        int y1 = calendar1.get(1);
        int y2 = calendar2.get(1);
        int d1 = calendar1.get(6);
        int d2 = calendar2.get(6);
        if (y1 - y2 > 0) {
            return (d1 - d2) + calendar2.getActualMaximum(6);
        } else if (y1 - y2 >= 0) {
            return d1 - d2;
        } else {
            return (d1 - d2) - calendar1.getActualMaximum(6);
        }
    }

    public static int getOffectHour(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        return (calendar1.get(11) - calendar2.get(11)) + (getOffectDay(date1, date2) * 24);
    }

    public static int getOffectMinutes(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        return (calendar1.get(12) - calendar2.get(12)) + (getOffectHour(date1, date2) * 60);
    }

    public static String getFirstDayOfWeek(String format) {
        return getDayOfWeek(format, 2);
    }

    public static String getLastDayOfWeek(String format) {
        return getDayOfWeek(format, 1);
    }

    private static String getDayOfWeek(String format, int calendarField) {
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            int week = c.get(7);
            if (week == calendarField) {
                return mSimpleDateFormat.format(c.getTime());
            }
            int offectDay = calendarField - week;
            if (calendarField == 1) {
                offectDay = 7 - Math.abs(offectDay);
            }
            c.add(5, offectDay);
            return mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFirstDayOfMonth(String format) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.set(5, 1);
            return mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }
    }

    public static String getLastDayOfMonth(String format) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.set(5, 1);
            c.roll(5, -1);
            return mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }
    }

    public static long getFirstTimeOfDay() {
        try {
            return getDateByFormat(getCurrentDate("yyyy-MM-dd") + " 00:00:00", dateFormatYMDHMS).getTime();
        } catch (Exception e) {
            return -1;
        }
    }

    public static long getLastTimeOfDay() {
        try {
            return getDateByFormat(getCurrentDate("yyyy-MM-dd") + " 24:00:00", dateFormatYMDHMS).getTime();
        } catch (Exception e) {
            return -1;
        }
    }

    public static boolean isLeapYear(int year) {
        if ((year % 4 != 0 || year % 400 == 0) && year % 400 != 0) {
            return false;
        }
        return true;
    }

    public static String formatDateStr2Desc(String strDate, String outFormat) {
        DateFormat df = new SimpleDateFormat(dateFormatYMDHMS);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c2.setTime(df.parse(strDate));
            c1.setTime(new Date());
            int d = getOffectDay(c1.getTimeInMillis(), c2.getTimeInMillis());
            if (d == 0) {
                int h = getOffectHour(c1.getTimeInMillis(), c2.getTimeInMillis());
                if (h > 0) {
                    return "今天" + getStringByFormat(strDate, dateFormatHM);
                }
                if (h >= 0 && h == 0) {
                    int m = getOffectMinutes(c1.getTimeInMillis(), c2.getTimeInMillis());
                    if (m > 0) {
                        return m + "分钟前";
                    }
                    if (m >= 0) {
                        return "刚刚";
                    }
                }
            } else if (d <= 0 ? d >= 0 || d == -1 || d == -2 : d == 1 || d == 2) {
            }
            String out = getStringByFormat(strDate, outFormat);
            if (out.isEmpty()) {
                return out;
            }
        } catch (Exception e) {
        }
        return strDate;
    }

    public static String getWeekNumber(String strDate, String inFormat) {
        String week = "星期日";
        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(new SimpleDateFormat(inFormat).parse(strDate));
            switch (calendar.get(7) - 1) {
                case 0:
                    week = "星期日";
                    break;
                case 1:
                    week = "星期一";
                    break;
                case 2:
                    week = "星期二";
                    break;
                case 3:
                    week = "星期三";
                    break;
                case 4:
                    week = "星期四";
                    break;
                case 5:
                    week = "星期五";
                    break;
                case 6:
                    week = "星期六";
                    break;
            }
            return week;
        } catch (Exception e) {
            return "错误";
        }
    }

    public static String getTimeQuantum(String strDate, String format) {
        if (getDateByFormat(strDate, format).getHours() >= 12) {
            return PM;
        }
        return AM;
    }

    public static String getTimeDescription(long milliseconds) {
        if (milliseconds <= 1000) {
            return milliseconds + "毫秒";
        }
        if ((milliseconds / 1000) / 60 <= 1) {
            return (milliseconds / 1000) + "秒";
        }
        return ((milliseconds / 1000) / 60) + "分" + ((milliseconds / 1000) % 60) + "秒";
    }

    public static boolean isToday(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        if (fmt.format(date).toString().equals(fmt.format(new Date()).toString())) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(formatDateStr2Desc("2017-8-10 12:2:20", "MM月dd日  HH:mm"));
    }

    public void getUTC(byte[] bytes) {
        Calendar cal = Calendar.getInstance();
        setTimeBytes(bytes, 0, cal.get(13));
        int pos = 0 + 4;
        setTimeBytes(bytes, pos, cal.get(12));
        pos += 4;
        setTimeBytes(bytes, pos, cal.get(11));
        pos += 4;
        setTimeBytes(bytes, pos, cal.get(5));
        pos += 4;
        setTimeBytes(bytes, pos, cal.get(2));
        pos += 4;
        setTimeBytes(bytes, pos, cal.get(1) - 1900);
        pos += 4;
        setTimeBytes(bytes, pos, cal.get(7));
        pos += 4;
        setTimeBytes(bytes, pos, cal.get(6));
        pos += 4;
        int zoneOffset = cal.get(15);
        int dstOffset = cal.get(16);
        setTimeBytes(bytes, pos, dstOffset);
        pos += 4;
        cal.add(14, -(zoneOffset + dstOffset));
        setUtc(bytes, pos, cal.getTimeInMillis());
    }

    private void setTimeBytes(byte[] bytes, int pos, int value) {
        for (int i = 0; i < 4; i++) {
            bytes[pos + i] = (byte) ((value >> (i * 8)) & 255);
        }
    }

    private void setUtc(byte[] bytes, int pos, long value) {
        for (int i = 0; i < 8; i++) {
            bytes[pos + i] = (byte) ((int) (255 & (value >> (i * 8))));
        }
    }

    public static String[] getStringByFormat2(long milliseconds, String format) {
        String thisDateTime = null;
        try {
            thisDateTime = new SimpleDateFormat(format).format(Long.valueOf(milliseconds));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] ret = thisDateTime.split(" ");
        ret[1] = ret[1].replace("-", " ");
        return ret;
    }

    public static String getStringByFormat3(long milliseconds, String format) {
        String thisDateTime = null;
        try {
            return new SimpleDateFormat(format).format(Long.valueOf(milliseconds));
        } catch (Exception e) {
            e.printStackTrace();
            return thisDateTime;
        }
    }
}
