package lm.util;

import java.util.Calendar;
import java.util.TimeZone;

import lm.date.Date;

/**
 * 天文算法
 * Created by limin on 16/01/12.
 *
 * @see <a>http://scienceworld.wolfram.com</a>
 */
public class Astronomical {
	public static double getJulianDay(int year, int month, int day, int hour, int minute, int second, TimeZone zone) {
		long milliseconds = 1000 * (second + (minute + hour * 60) * 60) - zone.getRawOffset();
		double d = milliseconds / 86400000.0 + day;
		return 367 * year
				- (7 * (year + ((month + 9) / 12)) / 4)
				- (3 * (((year + (month - 9) / 7) / 100) + 1) / 4)
				+ (275 * month / 9) + d + 1721028.5;
	}

	public static double getJulianDay(int year, int month, int day) {
		return getJulianDay(year, month, day, 0, 0, 0, TimeZone.getDefault());
	}

	public static double getJulianDay(long millis, TimeZone zone) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(zone);
		calendar.setTimeInMillis(millis);
		return getJulianDay(
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DATE),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND),
				calendar.getTimeZone());
	}

	public static double getJulianDay(Date date) {
		return getJulianDay(
				date.getYear(),
				date.getMonth() + 1,
				date.getDay(),
				0,
				0,
				0,
				date.getTimeZone());
	}

	public static double getMoonCycle(int year, int month, double day) {
		double jd = getJulianDay(year, month, (int) day) - 0.5;
		return (jd - 2449128.59) / 29.53058867;
	}
}
