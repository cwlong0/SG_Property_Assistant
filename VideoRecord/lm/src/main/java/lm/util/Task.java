package lm.util;

public abstract class Task {
	private final Object mLock = new Object();

	public final void run() throws Exception {
		onRun();
		onStop();
	}

	final void finish() {
		onFinish();
	}

	public abstract void onRun() throws Exception;

	protected void onFinish() {
	}

	protected void onStop() {
	}

	final public void lock() throws InterruptedException {
		synchronized(mLock) {
			mLock.wait();
		}
	}

	public final void unlock() {
		synchronized(mLock) {
			mLock.notify();
		}
	}
}