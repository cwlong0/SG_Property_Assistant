package lm.util;

import android.util.Log;

/**
 * Created by limin on 2016/03/16.
 */
public class Stopwatch {
	private long time;

	public void start() {
		time = System.currentTimeMillis();
	}

	public void log(String tag, String name) {
		long curr = System.currentTimeMillis();
		Log.i(tag, name + ": " + (curr - time) + " (ms)");
	}
}
