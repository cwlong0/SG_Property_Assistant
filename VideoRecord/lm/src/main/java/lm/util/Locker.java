package lm.util;

/**
 * Created by limin on 2016-01-25.
 */
public class Locker {
	private final Object mLock = new Object();

	private boolean isLocked;

	public void lock() {
		synchronized(mLock) {
			if(!isLocked) {
				try {
					isLocked = true;
					mLock.wait();
					isLocked = false;
				}
				catch(InterruptedException e) {
					System.err.print("Lock failed!");
					isLocked = false;
				}
			}
		}
	}

	public void unlock() {
		synchronized(mLock) {
			if(isLocked) {
				mLock.notify();
				isLocked = false;
			}
		}
	}

	public boolean isLocked() {
		return isLocked;
	}
}
