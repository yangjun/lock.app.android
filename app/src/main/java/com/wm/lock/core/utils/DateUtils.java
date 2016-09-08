package com.wm.lock.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("MM-dd");
	private static final SimpleDateFormat DATE_FORMAT3 = new SimpleDateFormat("yyyy-MM");
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat TIME_FORMAT2 = new SimpleDateFormat("HH:mm");
	private static final SimpleDateFormat TIME_FORMAT3 = new SimpleDateFormat("MM-dd HH:mm");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DATETIME_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	/**
	 * 主要用于显示类似刚刚，几分钟前，几小时前，昨天等等
	 * @param date Date对象
	 * @return
	 */
	public static String getDate(Date date) {
		// 定义最终返回的结果字符串。
		String dateString = null;

		long millisecond = new Date().getTime() - date.getTime();

		long second = millisecond / 1000;

		if (second <= 0) {
			second = 0;
		}

		if (second == 0) {
			dateString = "刚刚";
		} else if (second < 30) {
			dateString = second + "秒以前";
		} else if (second >= 30 && second < 60) {
			dateString = "半分钟之前";
		} else if (second >= 60 && second < 60 * 60) {
			long minute = second / 60;
			dateString = minute + "分钟前";
		} else if (second >= 60 * 60 && second < 60 * 60 * 24) {
			long hour = (second / 60) / 60;
			if (hour <= 3) {
				dateString = hour + "小时前";
			} else {
				dateString = "今天" + getFormatTime(date, "hh:mm");
			}
		} else if (second >= 60 * 60 * 24 && second <= 60 * 60 * 24 * 2) {
			dateString = "昨天" + getFormatTime(date, "hh:mm");
		} else if (second >= 60 * 60 * 24 * 2 && second <= 60 * 60 * 24 * 7) {
			long day = ((second / 60) / 60) / 24;
			dateString = day + "天前";
		} else if (second >= 60 * 60 * 24 * 7) {
			dateString = getFormatTime(date, "MM-dd hh:mm");
		} else if (second >= 60 * 60 * 24 * 365) {
			dateString = getFormatTime(date, "YYYY-MM-dd hh:mm");
		} else {
			dateString = "0"; //为负数的情况
		}
		// 最后返回处理后的结果。
		return dateString;
	}
	
	/**
	 * 格式转换
	 * @param date date对象
	 * @param formatString 格式类型string
	 * @return
	 */
	private static String getFormatTime(Date date, String formatString) {   
        return (new SimpleDateFormat(formatString)).format(date);   
    } 
	
	/**
	 * 转换日期为：yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String getDateStr(Date date) {
        return DATE_FORMAT.format(date);
    }
	
	/**
	 * 转换日期为：MM-dd
	 * @param date
	 * @return
	 */
	public static String getDateStr2(Date date) {
		return DATE_FORMAT2.format(date);
	}

	public static String getDateTimeStr2(Date date) {
		return TIME_FORMAT2.format(date);
	}

	/**
	 * 转换日期为：yyyy-MM
	 * @param date
	 * @return
	 */
	public static String getDateStr3(Date date) {
		return DATE_FORMAT3.format(date);
	}
	
	/**
	 * 转换日期为：yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String getDateTimeStr(Date date) {
		if (date == null) {
			return null;
		}
		return DATETIME_FORMAT.format(date);
	}
	  
	/**
	 * 转换日期为：HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String getTimeStr(Date date) {
		return TIME_FORMAT.format(date);
	}
	
	/**
	 * 转换日期为：HH:mm
	 * @param date
	 * @return
	 */
	public static String getTimeStr2(Date date) {
		return TIME_FORMAT2.format(date);
	}
	
	/**
	 * 转换日期为：MM-dd HH:mm
	 * @param date
	 * @return
	 */
	public static String getTimeStr3(Date date) {
		return TIME_FORMAT3.format(date);
	}
	
	/**
	 * 转换日期字符串为：yyyy-MM-dd
	 * @param s
	 * @return
	 */
	public static Date parseDate(String s) {
		try {
			return DATE_FORMAT.parse(s);
		} catch (Exception e) {
			return new Date();
		}
    }
	
	/**
	 * 转换为：yyyy-MM-dd
	 * @param s
	 * @return
	 */
	public static Date parseDate(Object value) {
		return parseDate(String.valueOf(value));
    }
	
	/**
	 * 转换日期字符串为：yyyy-MM-dd HH:mm:ss
	 * @param s
	 * @return
	 */
	public static Date parseDateTime(String s) {
		try {
			return DATETIME_FORMAT.parse(s);
		} catch (Exception e) {
			return new Date();
		}
	}
	 
	
	/**
	 * 转换日期字符串为：HH:mm
	 * @param s
	 * @return
	 */
	public static Date parseDateTime2(String s) {
		Date date = new Date();
		try {
			String day = DATE_FORMAT.format(date);
			return DATETIME_FORMAT2.parse(day + " " + s);
		} catch (Exception e) {
			return date;
		}
	}
	
	/**
	 * 转换为：yyyy-MM-dd HH:mm:ss
	 * @param s
	 * @return
	 */
	public static Date parseDateTime(Object value) {
		return parseDateTime(String.valueOf(value));
	}
	
}
