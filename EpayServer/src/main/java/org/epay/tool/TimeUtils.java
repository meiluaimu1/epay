
package org.epay.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.grain.httpserver.HttpConfig;

public class TimeUtils {
	public static SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static String dateToString(Date date) {
		if (date == null) {
			return null;
		}
		return longDateFormat.format(date);
	}

	public static Date stringToDate(String time) {
		if (StringUtil.stringIsNull(time)) {
			return null;
		}
		Date date;
		try {
			date = longDateFormat.parse(time);
			return date;
		} catch (ParseException e) {
			HttpConfig.log.error("TimeUtils.stringToDate error", e);
			return null;
		}

	}

	public static String getDateStartString(Date date) {
		return shortDateFormat.format(date) + " 00:00:00";
	}
}
