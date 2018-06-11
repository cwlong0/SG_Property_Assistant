package lm.date;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 日期
 */
public final class Date implements Cloneable, Comparator<Date> {
	private final static Calendar sCalendar;

	public final static int YEAR = Calendar.YEAR;

	public final static int MONTH = Calendar.MONTH;

	public final static int DAY = Calendar.DATE;

	public final static int WEEK = Calendar.DAY_OF_WEEK;

	@IntDef({YEAR, MONTH, DAY})
	@Retention(RetentionPolicy.RUNTIME)
	@interface Field{}

	static {
		sCalendar = Calendar.getInstance();
		sCalendar.clear();
	}

	private Calendar mCalendar = (Calendar) sCalendar.clone();

	public Date(int year, int month, int day) {
		set(year, month, day);
	}

	public Date(long t) {
		setTimeInMillis(t);
	}

	public Date(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		try {
			setTimeInMillis(format.parse(date).getTime());
		}
		catch(ParseException e) {
			throw new Error("Date format must be \"yyyy-MM-dd\". Such as: 2016-01-01");
		}
	}

	public Date() {
		this(System.currentTimeMillis());
	}

	public int getYear() {
		return mCalendar.get(YEAR);
	}

	public Date setYear(int year) {
		mCalendar.set(YEAR, year);
		return this;
	}

	public int getMonth() {
		return mCalendar.get(MONTH);
	}

	public Date setMonth(int month) {
		mCalendar.set(MONTH, month);
		return this;
	}

	public int getDay() {
		return mCalendar.get(DAY);
	}

	public Date setDay(int day) {
		mCalendar.set(DAY, day);
		return this;
	}

	public Date set(int year, int month, int day) {
		setYear(year);
		setMonth(month);
		setDay(day);
		return this;
	}

	public Date set(Date date) {
		set(date.getYear(), date.getMonth(), date.getDay());
		return this;
	}

	public int getWeek() {
		return mCalendar.get(WEEK);
	}

	public long getTimeInMillis() {
		return mCalendar.getTimeInMillis();
	}

	public Date setTimeInMillis(long time) {
		mCalendar.setTimeInMillis(time);
		int year = mCalendar.get(YEAR);
		int month = mCalendar.get(MONTH);
		int day = mCalendar.get(DAY);
		mCalendar.clear();
		return set(year, month, day);
	}

	public Date setTimeZone(TimeZone zone) {
		mCalendar.setTimeZone(zone);
		return this;
	}

	public TimeZone getTimeZone() {
		return mCalendar.getTimeZone();
	}

	public Date offsetByField(@Field int field, int offset) {
		switch(field) {
		case YEAR:
			return setYear(getYear() + offset);

		case MONTH:
			return setMonth(getMonth() + offset);

		case DAY:
		default:
			return setDay(getDay() + offset);
		}
	}

	@Override
	public Date clone() {
		try {
			Date date = (Date) super.clone();
			date.mCalendar = (Calendar) mCalendar.clone();
			return date;
		}
		catch(CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}

	public String toShortString() {
		return String.format(
				Locale.ENGLISH,
				"%04d - %02d - %02d",
				getYear(),
				getMonth() + 1,
				getDay());
	}

	@Override
	public String toString() {
		return String.format(
				Locale.ENGLISH,
				"%s@0x%08X: %s",
				getClass().getName(),
				hashCode(),
				toShortString());
	}

	/**
	 * 返回格式为yyyy-MM-dd的日期，例如：2016-01-27
	 * @return
	 */
	public String toDateString() {
		return String.format(
				Locale.ENGLISH,
				"%04d-%02d-%02d",
				getYear(),
				getMonth() + 1,
				getDay());
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Date) {
			Date t = (Date) o;
			return t.getTimeInMillis() == getTimeInMillis();
		}

		return false;
	}

	public boolean equals(int year, int month, int day) {
		return getYear() == year && getMonth() == month && getDay() == day;
	}

	@Override
	public int compare(Date lhs, Date rhs) {
		return offset(lhs, rhs);
	}

	/**
	 * {@code t1},{@code t2} 间隔天数
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static int offset(Date t1, Date t2) {
		return (int) TimeUnit.MILLISECONDS.toDays(
				t2.getTimeInMillis() - t1.getTimeInMillis());
	}

	/**
	 * {@code t1},{@code t2} 间隔天数
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static int offset(long t1, long t2) {
		return (int) TimeUnit.MILLISECONDS.toDays(t2 - t1);
	}

	public static int innerCount(Date t1, Date t2) {
		return offset(t1.getTimeInMillis(), t2.getTimeInMillis());
	}

	public static int innerCount(long t1, long t2) {
		return count(t1, t2) - 2;
	}

	/**
	 * {@code t1},{@code t2} 总共天数
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static int count(Date t1, Date t2) {
		return Math.abs(offset(t1, t2)) + 1;
	}

	public static int count(long t1, long t2) {
		return Math.abs(offset(t1, t2)) + 1;
	}

	/**
	 * 间隔天数
	 * @param t
	 * @param offset
	 */
	public static void offset(Date t, int offset) {
		final long ms = t.getTimeInMillis();

		t.mCalendar.setTimeInMillis(
				ms + TimeUnit.DAYS.toMillis(offset)
		);
	}

	public static long getTime(int year, int month, int day) {
		Calendar calendar = (Calendar) sCalendar.clone();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, day);
		return calendar.getTimeInMillis();
	}

	public static long getTime(String date) {
		try {
			String[] dates = date.split("\\-");
			int year = Integer.parseInt(dates[0]);
			int month = Integer.parseInt(dates[1]) - 1;
			int day = Integer.parseInt(dates[2]);
			return getTime(year, month, day);
		}
		catch(Exception e) {
			throw new Error(e.getMessage());
		}
	}

	public static String toDateString(int year, int month, int day) {
		return new Date(year, month, day).toDateString();
	}

	public static String toGTMString(long date, String template) {
		SimpleDateFormat fmt = new SimpleDateFormat(template, Locale.ENGLISH);
		fmt.setTimeZone(TimeZone.getTimeZone("GTM"));
		return fmt.format(date);
	}

	public static int toDays(long ms) {
		return (int) TimeUnit.MILLISECONDS.toDays(ms);
	}

	public static long toDayMilliseconds(int day) {
		return TimeUnit.DAYS.toMillis(day);
	}

	public static boolean isToday(int year, int month, int day) {
		Date today = today();
		return today.getYear() == year && today.getMonth() == month && today.getDay() == day;
	}

	public static Date today() {
		return new Date();
	}

	public static long todayInMillis() {
		return today().getTimeInMillis();
	}

	public static long day2milliseconds(int day) {
		return TimeUnit.DAYS.toMillis(day);
	}
}
