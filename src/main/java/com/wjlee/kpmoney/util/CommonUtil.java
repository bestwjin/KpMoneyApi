package com.wjlee.kpmoney.util;

import java.util.Calendar;
import java.util.Date;

public class CommonUtil {
	
	/**
	 * Date 계산
	 * @param nowDate
	 * @param type
	 * @param time
	 * @return
	 */
	public static Date addDate(Date nowDate, int type, int time) {
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(nowDate);
		cal.add(type, time);
		
		return cal.getTime();
	}
}
