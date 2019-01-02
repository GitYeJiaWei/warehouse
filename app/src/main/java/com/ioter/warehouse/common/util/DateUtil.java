package com.ioter.warehouse.common.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * 时间/日期处理工具类
 */
public class DateUtil {

    private static final String FORMAT_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat sdf = null;
    private static Calendar calendar = null;

    /**
     * 判断time是否为当天
     *
     * @param time
     * @return
     */
    public static boolean isCurrentDay(long time) {
        long timeMillis = System.currentTimeMillis();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String sp_time = sf.format(time);
        String current_time = sf.format(timeMillis);

        if (sp_time.equals(current_time)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是当月
     *
     * @return
     */
    public static boolean isCurrentMonth(long time) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTimeInMillis(time);
        c2.setTime(new Date());
        return ((c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)));
    }

    /**
     * 是否在同一天
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isTheSameDay(long milliseconds1, long milliseconds2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTimeInMillis(milliseconds1);
        c2.setTimeInMillis(milliseconds2);
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
                && (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 将字符串转化为Date
     *
     * @param str 传入字符串时间
     * @return
     */
    public static Date getStringToDate(String str) {

        sdf = new SimpleDateFormat(FORMAT_Y_M_D_H_M_S, Locale.getDefault());
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            DebugUtil.printe("Exception", "Exception:" + e);
        }
        return null;
    }

    /**
     * 获取年
     */
    public static int getYear(Date date) {

        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取月
     */
    public static int getMon(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (calendar.get(Calendar.MONTH) + 1);
    }

    /**
     * 获取日期天
     */
    public static int getDay(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取星期几
     */
    public static int getWeek(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 获取时间字符串 自定义格式
     */
    public static String getCustomStr(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);// 设置日期格式
        String nowdate = df.format(date);// new Date()为获取当前系统时间
        return nowdate;
    }

    /**
     * 获取时间数据 自定义格式
     */
    public static long getCustomLong(String time, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);// 设置日期格式
        long date = 0;
        try {
            date = df.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取当前时间年月 格式yyyy年M月
     */
    public static String getYearMonthStr(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年M月");// 设置日期格式
        String nowdate = df.format(date);// new Date()为获取当前系统时间
        return nowdate;
    }

    /**
     * 获取当前时间年月 格式yyyy年M月d日
     */
    public static String getYearMonthDayStr(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年M月d日");// 设置日期格式
        String nowdate = df.format(date);// new Date()为获取当前系统时间
        return nowdate;
    }

    /**
     * 将long 转成xxxx.xx.xx xx:xx
     *
     * @param time
     */
    public static String getStrDateYMdHm(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");// 设置日期格式
        String strDate = df.format(new Date(time));// new Date()为获取当前系统时间
        return strDate;
    }

    public static String getStrDatems(long time) {
        SimpleDateFormat df = new SimpleDateFormat("mm:ss");// 设置日期格式
        String strDate = df.format(new Date(time));// new Date()为获取当前系统时间
        return strDate;
    }

    /**
     * 将long 转成xxxx.xx.xx
     *
     * @param time
     */
    public static String getStrDateYMd(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");// 设置日期格式
        String strDate = df.format(new Date(time));// new Date()为获取当前系统时间
        return strDate;
    }

    /**
     * 获取当前时间 格式yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDateStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        String nowdate = df.format(new Date());// new Date()为获取当前系统时间
        return nowdate;
    }

    /**
     * 获取当前时间标识码精确到毫秒 格式yyyyMMddHHmmssSSS
     */
    public static String getNowtimeKeyStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");// 设置日期格式
        String nowdate = df.format(new Date());// new Date()为获取当前系统时间
        return nowdate;
    }

    /**
     * 获取当前时间标识码精确到秒 格式yyyyMMddHHmmss
     */
    public static String getNowDateKeyStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
        String nowdate = df.format(new Date());// new Date()为获取当前系统时间
        return nowdate;
    }

    /**
     * 通过long获取时间字符串 格式yyyy-mm-dd HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String getStrDate(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        String strDate = df.format(new Date(time));// new Date()为获取当前系统时间
        return strDate;
    }

    /**
     * 通过long获取时间字符串 格式yyyy-mm-dd HH:mm
     *
     * @param time
     * @return
     */
    public static String getStrDateyMDHm(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
        String strDate = df.format(new Date(time));// new Date()为获取当前系统时间
        return strDate;
    }

    /**
     * 将long 转成 MM-dd HH:mm
     *
     * @param time
     * @return
     */
    public static String getStrDateMDHm(long time) {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");// 设置日期格式
        String strDate = df.format(new Date(time));// new Date()为获取当前系统时间
        return strDate;
    }

    public static String getStrDateYMD(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        String strDate = df.format(new Date(time));// new Date()为获取当前系统时间
        return strDate;
    }

    public static String getStrDateYM(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");// 设置日期格式
        String strDate = df.format(new Date(time));// new Date()为获取当前系统时间
        return strDate;
    }

    /**
     * 将long 转成 HH:mm
     *
     * @param time
     */
    public static String getStrDateHm(long time) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
        String strDate = df.format(new Date(time));// new Date()为获取当前系统时间
        return strDate;
    }

    public static String getStrDateMd(long time) {
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日");// 设置日期格式
        String strDate = df.format(new Date(time));// new Date()为获取当前系统时间
        return strDate;
    }

    /**
     * @param time
     * @return xx月
     */
    public static String getStrDateMonth(long time) {
        SimpleDateFormat df = new SimpleDateFormat("MM月");// 设置日期格式
        String strDate = df.format(new Date(time));// new Date()为获取当前系统时间
        return strDate;
    }


    public static String getStrDateMd2(long time) {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd");// 设置日期格式
        String strDate = df.format(new Date(time));// new Date()为获取当前系统时间
        return strDate;
    }

    /**
     * 经过的时长转换 x分钟x秒
     */
    public static String getDurationStr(int duration) {
        int sec = duration / 1000;
        String durationStr = "";
        if (duration / 1000 > 60) {

            int min = sec / 60;
            durationStr = min + " 分钟  ";
            sec = sec - min * 60;
        }

        durationStr += sec + " 秒 ";
        return durationStr;
    }

    /**
     * 经过的时长转换 x分钟x秒
     */
    public static String getDurationDigitStr(int duration) {
        int sec = duration / 1000;
        String durationStr = "";
        String minStr = "00";
        String secStr = "";

        if (duration / 1000 > 60) {
            int min = sec / 60;
            minStr = min / 10 + "" + min % 10;
            sec = sec - min * 60;
        }

        secStr = sec / 10 + "" + sec % 10;
        durationStr = minStr + ":" + secStr;
        return durationStr;
    }

    /**
     * 将long型转化成yyyy-MM-dd格式的字符串
     *
     * @param lDate
     * @return
     * @throws ParseException
     */
    public static String getDate2(long lDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 本地时区
        Calendar nowCal = Calendar.getInstance();
        TimeZone localZone = nowCal.getTimeZone();
        // 设定SDF的时区为本地
        sdf.setTimeZone(localZone);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        // im_shezhi DateFormat的时间区域为GMT
        sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
        int baseTime = 1970;
        String strParse = baseTime + "-01-01 00:00:00";

        String s = sdf.format(0);
        try {
            Date beginTime = sdf1.parse(strParse);
            long intverval = lDate * 1000 + beginTime.getTime();
            s = sdf.format(intverval);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;

    }

    /**
     * @param time 将秒转 xx:xx:xx 小于一小时xx:xx 分秒
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    /**
     * @param time 将秒转 xx:xx:xx
     * @return
     */
    public static String secToTimeWithHour(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "00:00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    /**
     * @param time 将秒转 xx:xx, 不含秒
     * @return
     */
    public static String secToTimeWithOutSec(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        if (time <= 0) {
            return "00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                timeStr = "00:" + unitFormat(minute);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59";
                minute = minute % 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 获取聊天显示的时间
     *
     * @param value 微秒
     * @return
     */
    public static String getChatDateByMs(long value) {
        if (value == 0) {
            return "";
        }
        value = value / 1000;
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        long todayTime = currentTime - (date.getHours() * 3600 + date.getMinutes() * 60 + date.getSeconds()) * 1000;
        long timeLen = todayTime - value;
        if (timeLen <= 0) {
            return new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date(value));// 16:58
        } else if (timeLen < 86400000) {
            return "昨天" + new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date(value));// 昨天
            // 16:58
        } else if (timeLen < 2592000000L) {
            return new SimpleDateFormat("MM月dd日", Locale.ENGLISH).format(new Date(value));// 10月16日
        }
        return new SimpleDateFormat("yyyy年MM月dd日", Locale.ENGLISH).format(new Date(value));// 2015年7月8日
    }

    /**
     * 获取聊天显示的时间
     *
     * @param value 秒
     * @return
     */
    public static String getChatDate(long value) {
        if (value == 0) {
            return "";
        }
        value = value * 1000;
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        long todayTime = currentTime - (date.getHours() * 3600 + date.getMinutes() * 60 + date.getSeconds()) * 1000;
        long timeLen = todayTime - value;
        if (timeLen <= 0) {
            return new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date(value));// 16:58
        } else if (timeLen < 86400000) {
            return "昨天" + new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date(value));// 昨天
            // 16:58
        } else if (timeLen < 2592000000L) {
            return new SimpleDateFormat("MM月dd日", Locale.ENGLISH).format(new Date(value));// 10月16日
        }
        return new SimpleDateFormat("yyyy年MM月dd日", Locale.ENGLISH).format(new Date(value));// 2015年7月8日
    }

    /**
     * 获取加鱼活动时间显示
     *
     * @param value 秒
     * @return
     */
    public static String getPondDate(long value) {
        if (value == 0) {
            return "";
        }
        value = value * 1000;
        if (isCurrentDay(value)) {
            return new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date(value));// 16:58
        } else {
            return new SimpleDateFormat("MM月dd日 HH:mm", Locale.ENGLISH).format(new Date(value));// 10月16日
        }
    }

    /**
     * 获取鱼酬显示的时间
     *
     * @param value
     * @return
     */
    public static String getDetailDate(long value) {
        if (value == 0) {
            return "";
        }
        value = value * 1000;
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        long todayTime = currentTime - (date.getHours() * 3600 + date.getMinutes() * 60 + date.getSeconds()) * 1000;
        long timeLen = todayTime - value;
        if (timeLen <= 0) {
            return new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date(value));// 16:58
        } else if (timeLen < 86400000) {
            return "昨天" + new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date(value));// 昨天
            // 16:58
        } else if (timeLen < 2592000000L) {
            return new SimpleDateFormat("MM-dd HH:mm", Locale.ENGLISH).format(new Date(value));// 10-16 13:14
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH).format(new Date(value));// 2012-10-16 13:14
    }

    /**
     * 获取鱼酬显示的时间
     *
     * @param value
     * @return
     */
    public static String getDetailDate2(long value) {
        if (value == 0) {
            return "";
        }
        value = value * 1000;
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        long todayTime = currentTime - (date.getHours() * 3600 + date.getMinutes() * 60 + date.getSeconds()) * 1000;
        long timeLen = todayTime - value;
        if (timeLen < 2592000000L) {
            return new SimpleDateFormat("MM月dd日 HH:mm", Locale.ENGLISH).format(new Date(value));// 10月16日 13:14
        }
        return new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.ENGLISH).format(new Date(value));// 2015年07月08日 13:14
    }

    /**
     * 获取时间
     *
     * @param context
     * @param strDate
     * @return
     */
    public static String getCircleDate(long value) {
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        long todayTime = currentTime - (date.getHours() * 3600 + date.getMinutes() * 60 + date.getSeconds()) * 1000;
        long timeLen = todayTime - value;
        if (timeLen < 2592000000L) {
            return new SimpleDateFormat("MM-dd HH:mm", Locale.ENGLISH).format(new Date(value));// 10月16日
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH).format(new Date(value));// 2015年7月8日
    }

    /**
     * 获取个榜显示时间
     *
     * @param context
     * @param strDate
     * @return
     */
    public static Spannable getCycleDate(Context context, String strDate) {
        if ((strDate == null) || strDate.equals(""))
            return null;
        Spannable sp = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            Date date = sdf.parse(strDate);

            Date today = new Date();

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String strToday = sdf1.format(today);
            strToday += " 00:00:00";
            Date todayStart = sdf.parse(strToday);
            long interval = todayStart.getTime() - date.getTime();

            String strRet = "";
            if (interval < 0) // 今天
            {
                sp = new SpannableString("今天");
                sp.setSpan(new RelativeSizeSpan(2.0f), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            } else if (interval < 86400000)// 昨天
            {
                sp = new SpannableString("昨天");
                sp.setSpan(new RelativeSizeSpan(2.0f), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            } else if (interval < (2 * 86400000))// 前天)
            {
                sp = new SpannableString("前天");
                sp.setSpan(new RelativeSizeSpan(2.0f), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM", Locale.ENGLISH);
                strRet = dateFormat.format(date);
                sp = new SpannableString(strRet);
                sp.setSpan(new RelativeSizeSpan(2.0f), 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sp;
    }

    // 今天的显示“几小时前”，昨天以前的显示时间，时间只需要时和分
    public static String getCycleTime(Context context, String strDate) {
        if ((strDate == null) || strDate.equals(""))
            return "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            Date date = sdf.parse(strDate);

            Date today = new Date();
            long interval = Math.abs(today.getTime() - date.getTime());
            today.setHours(23);
            today.setMinutes(59);
            today.setSeconds(59);
            boolean isToday = (interval / 86400000) == 0 ? true : false;
            if (isToday) {
                if (interval < 60 * 1000) {
                    return "刚刚";
                } else if (interval < 60 * 60 * 1000) {
                    return (interval / (60 * 1000)) + "分钟前";
                } else {
                    return (interval / (60 * 60 * 1000)) + "小时前";
                }
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                return dateFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取居住圈活动起止时间显示 例1: 06-04 12:00~14:00 例2: 06-04 12:00 ~ 06-05 13:00
     */
    public static String getCircleHomeActivityTime(long startTime, long endTime) {
        String str = "";
        if (startTime == 0 || endTime == 0 || (startTime > endTime)) {
            return str;
        }
        if (DateUtil.isTheSameDay(startTime, endTime)) {
            str += DateUtil.getStrDateMd2(startTime) + "  " + DateUtil.getStrDateHm(startTime) + " ~ "
                    + DateUtil.getStrDateHm(endTime);
        } else {
            str += DateUtil.getStrDateMDHm(startTime) + " ~ " + DateUtil.getStrDateMDHm(endTime);
        }
        return str;
    }

    /**
     * 将文本中的数字转成中文数字
     *
     * @param cNumber
     * @return
     */
    public static String getNumerToString(String cNumber) {

        Map<Character, Character> numMap = new HashMap<Character, Character>(10);
        numMap.put('1', '一');
        numMap.put('2', '二');
        numMap.put('3', '三');
        numMap.put('4', '四');
        numMap.put('5', '五');
        numMap.put('6', '六');
        numMap.put('7', '七');
        numMap.put('8', '八');
        numMap.put('9', '九');
        numMap.put('0', '零');

        char[] chars = cNumber.toCharArray();

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            Character number = numMap.get(chars[i]);
            if (number != null) {
                result.append(number);
            } else {
                result.append(chars[i]);
            }
        }

        return result.toString();
    }

    /**
     * 获取黄金积分显示的时间
     *
     * @param value 秒
     * @return
     */
    public static String getGoldPointDate(long value) {
        if (value == 0) {
            return "";
        }
        value = value * 1000;
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        long todayTime = currentTime - (date.getHours() * 3600 + date.getMinutes() * 60 + date.getSeconds()) * 1000;
        long timeLen = todayTime - value;
        if (timeLen <= 0) {
            return new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date(value));// 16:58
        } else if (timeLen < 86400000) {
            return "昨天" + new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date(value));// 昨天
            // 16:58
        } else if (timeLen < 2592000000L) {
            return new SimpleDateFormat("MM-dd", Locale.ENGLISH).format(new Date(value));// 10-23
        }
        return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date(value));// 2017-10-23
    }

    /**
     * 日期选择
     *
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                tv.setText("您选择了：" + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * 时间选择
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public static void showTimePickerDialog(Activity activity,int themeResId, final TextView tv, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog( activity,themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv.setText("您选择了：" + hourOfDay + "时" + minute  + "分");
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                ,true).show();
    }
}