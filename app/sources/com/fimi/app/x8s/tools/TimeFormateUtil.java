package com.fimi.app.x8s.tools;

public class TimeFormateUtil {
    public static String getRecordTime(int time) {
        if (time == 0) {
            return "00:00";
        }
        if (time < 60) {
            if (time < 10) {
                return "00:0" + time;
            }
            return "00:" + time;
        } else if (time < 60) {
            return "00:00";
        } else {
            int min = time / 60;
            int sencode = time % 60;
            if (min < 10) {
                if (sencode < 10) {
                    return "0" + min + ":" + "0" + sencode;
                }
                return "0" + min + ":" + sencode;
            } else if (sencode < 10) {
                return min + ":" + "0" + sencode;
            } else {
                return min + ":" + sencode;
            }
        }
    }

    public static String getRecordTime(int hour, int min, int second) {
        String recordTime = "00:00";
        if (hour <= 0) {
            if (min > 0) {
                if (min < 10) {
                    if (second >= 10) {
                        return "0" + min + ":" + second;
                    }
                    return "0" + min + ":0" + second;
                } else if (second >= 10) {
                    return min + ":" + second;
                } else {
                    return min + ":0" + second;
                }
            } else if (second >= 10) {
                return "00:" + second;
            } else {
                return "00:0" + second;
            }
        } else if (min > 0) {
            if (min < 10) {
                if (second >= 10) {
                    return hour + ":0" + min + ":" + second;
                }
                return hour + ":0" + min + ":0" + second;
            } else if (second >= 10) {
                return hour + ":" + min + ":" + second;
            } else {
                return hour + ":" + min + ":0" + second;
            }
        } else if (second >= 10) {
            return hour + ":" + "00" + ":" + second;
        } else {
            return hour + ":" + "00" + ":0" + second;
        }
    }

    public static void main(String[] args) {
        System.out.println(getRecordTime(0, 59, 30));
    }
}
