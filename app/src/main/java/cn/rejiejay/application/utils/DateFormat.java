package cn.rejiejay.application.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormat {
    Calendar cal;


    public DateFormat() {
        cal = Calendar.getInstance();
    }

    public DateFormat(Date date) {
        cal = Calendar.getInstance();
        cal.setTime(date);
    }

    public int getFullYear() {
        return cal.get(Calendar.YEAR);
    }

    public int getMonth() {
        return cal.get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public String getWeekCn() {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;

        if (week_index < 0) {
            week_index = 0;
        }

        return weeks[week_index];
    }

    public int getWeekInMonth() {
        int week = cal.get(Calendar.DAY_OF_MONTH);

        if (week <= 7) {
            return 1;
        } else if (week <= 14) {
            return 2;
        } else if (week <= 21) {
            return 3;
        } else {
            return 4;
        }
    }

    public int getHour() {
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        return cal.get(Calendar.MINUTE);
    }

    public int getSecond() {
        return cal.get(Calendar.SECOND);
    }

    public int getMillisecond() {
        return cal.get(Calendar.MILLISECOND);
    }

    public String getYYmmDDww() {
        String yy = String.valueOf(getFullYear());
        String mm = String.valueOf(getMonth());;
        String dd = String.valueOf(getDay());
        String week = getWeekCn();
        String weekInMonth = String.valueOf(getWeekInMonth());

        return yy + "y " + mm + "月 " + dd + "d 第" + weekInMonth + "周 " + week;
    }
    
    public static long getTimeByyyyyMMdd(int yyyy, int mm, int dd) {
    	
    	String dateString = "";
    	
    	dateString += String.valueOf(yyyy);
    	
    	dateString += "-";
    	
    	if (mm < 10) {
    		dateString += "0";
    		dateString += String.valueOf(mm);
    	} else {
    		dateString += String.valueOf(mm);
    	}
    	
    	dateString += "-";
    	
    	if (dd < 10) {
    		dateString += "0";
    		dateString += String.valueOf(dd);
    	} else {
    		dateString += String.valueOf(dd);
    	}
    	
		try {
			Date date =  new SimpleDateFormat("yyyy-MM-dd").parse(dateString);

			return date.getTime();
			
		} catch (ParseException e) {
			// 一般都不会有错的
			return new Date().getTime();
		}
    }
}
