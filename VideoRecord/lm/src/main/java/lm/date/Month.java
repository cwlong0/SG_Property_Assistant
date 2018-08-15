package lm.date;

import java.util.Calendar;
import java.util.Locale;

/**
 * 月份信息
 * <p/>
 * Created by limin on 15/10/12.
 */
public final class Month implements Cloneable{
	/**
	 * 平年每月的天数
	 */
	private final static int[] DEF_DAYS_OF_MONTH =
			{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	/**
	 * 闰年每月的天数
	 */
	private final static int[] LEAP_DAYS_OF_MONTH =
			{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	private final Date start;

	private final Date end;

	/**
	 * {@code true}闰年，{@code false}平年
	 */
	private final boolean isLeap;

	/**
	 * 该月的天数
	 */
	private final int length;

	public Month() {
		this(new Date());
	}

	public Month(Date date) {
		this(date.getYear(), date.getMonth());
	}

	public Month(int year, int month) {
		this.start = new Date(year, month, 1);
		this.isLeap = ((year % 4) == 0 && ((year % 100) != 0 || (year % 400) == 0));
		this.length = this.isLeap ? LEAP_DAYS_OF_MONTH[this.start.getMonth()] : DEF_DAYS_OF_MONTH[this.start.getMonth()];
		this.end = this.start.clone().setDay(length);
	}

	/**
	 * 前一个月
	 *
	 * @return
	 */
	public Month previous() {
		return new Month(start.clone().offsetByField(Date.MONTH, -1));
	}

	/**
	 * 下一个月
	 *
	 * @return
	 */
	public Month next() {
		return new Month(start.clone().offsetByField(Date.MONTH, 1));
	}

	/**
	 * @return 年份
	 */
	public int getYear() {
		return start.getYear();
	}

	/**
	 * @return 月份
	 * @see Calendar#JANUARY
	 * @see Calendar#FEBRUARY
	 * @see Calendar#MARCH
	 * @see Calendar#APRIL
	 * @see Calendar#MAY
	 * @see Calendar#JUNE
	 * @see Calendar#JULY
	 * @see Calendar#AUGUST
	 * @see Calendar#SEPTEMBER
	 * @see Calendar#OCTOBER
	 * @see Calendar#NOVEMBER
	 * @see Calendar#DECEMBER
	 */
	public int getMonth() {
		return start.getMonth();
	}

	/**
	 * 平年、闰年
	 *
	 * @return {@code true}为闰年，{@code false}为平年
	 */
	public boolean isLeap() {
		return this.isLeap;
	}

	/**
	 * 当月的总共天数
	 *
	 * @return
	 */
	public int getLength() {
		return this.length;
	}

	public long getStartInMillis() {
		return start.getTimeInMillis();
	}

	public Date getStart() {
		return start.clone();
	}

	public long getEndInMillis() {
		return end.getTimeInMillis();
	}

	public Date getEnd() {
		return end.clone();
	}

	/**
	 * 当月某一天的星期
	 *
	 * @param day 日期，从1日开始
	 * @return 星期 {@code 日 ~ 六}
	 * @see Calendar#SUNDAY
	 * @see Calendar#MONDAY
	 * @see Calendar#TUESDAY
	 * @see Calendar#WEDNESDAY
	 * @see Calendar#THURSDAY
	 * @see Calendar#FRIDAY
	 * @see Calendar#SATURDAY
	 */
	public int getWeek(int day) {
		if(day <= 0 && day > getLength()) {
			throw new Error("Date Error.");
		}

		Date date = start.clone();
		date.setDay(day);
		return date.getWeek();
	}

	public long getTime(int day) {
		Date date = start.clone();
		date.setDay(day);
		return date.getTimeInMillis();
	}

	@Override
	public boolean equals(Object o) {
		if(o != null && o instanceof Month) {
			Month m = (Month) o;
			return m.getYear() == getYear() && m.getMonth() == getMonth();
		}

		return false;
	}

	public boolean equals(int year, int month) {
		return getYear() == year && getMonth() == month;
	}

	@Override
	public String toString() {
		return String.format(Locale.ENGLISH,
				"%s --- Year: %04d Month: %02d\n",
				super.toString(),
				getYear(),
				getMonth() + 1);
	}

	@Override
	public Month clone() {
		try {
			return (Month) super.clone();
		}
		catch(CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}
}
