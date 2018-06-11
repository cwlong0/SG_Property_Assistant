package lm.util;

/**
 * Created by limin on 2016-02-02.
 */
public abstract class Builder<T> {

	public abstract T build();

	@Override
	public String toString() {
		return "" + build();
	}
}
