package com.fimi.kernel.utils;

import java.text.SimpleDateFormat;

public class DateFormater {
    public static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat();

    public static String dateString(long date, String pattern) {
        try {
            mSimpleDateFormat.applyPattern(pattern);
            return mSimpleDateFormat.format(Long.valueOf(date));
        } catch (Exception e) {
            return null;
        }
    }

    public static final String getTimeStr(long date) {
        long hour = (date / 3600) / 1000;
        long min = ((date / 60) / 1000) - (60 * hour);
        long sec = ((date / 1000) - (3600 * hour)) - (60 * min);
        StringBuilder stringBuilder = new StringBuilder();
        if (hour < 10) {
            stringBuilder.append("0" + hour);
        } else if (hour > 100) {
            stringBuilder.append(hour - hour);
        } else {
            stringBuilder.append(hour);
        }
        stringBuilder.append(":");
        if (min < 10) {
            stringBuilder.append("0" + min);
        } else {
            stringBuilder.append(min);
        }
        stringBuilder.append(":");
        if (sec < 10) {
            stringBuilder.append("0" + sec);
        } else {
            stringBuilder.append(sec);
        }
        return stringBuilder.toString();
    }
}
