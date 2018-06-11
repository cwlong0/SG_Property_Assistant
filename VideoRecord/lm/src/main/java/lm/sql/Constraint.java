package lm.sql;

/**
 * Created by limin on 2016-01-28.
 */
public enum Constraint {
	AUTOINCREMENT ("AUTOINCREMENT"),

	NOT_NULL ("NOT NULL"),

	NONE ("");

	private final String mNative;

	Constraint(String mNative) {
		this.mNative = mNative;
	}

	@Override
	public String toString() {
		return mNative;
	}
}
