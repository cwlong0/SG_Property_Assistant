package lm.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.concurrent.ArrayBlockingQueue;

public final class TaskManager extends Thread implements Handler.Callback {
	private final static String TAG = "TaskManager";

	private final Handler mHandler = new Handler(this);

	private final ArrayBlockingQueue<WeakReference<Task>> mWeakReferenceTasks = new ArrayBlockingQueue<>(100);

	private boolean mRunning;

	private boolean mStop;

	private OnTaskFinishListener mFinishListener;

	public TaskManager() {
		mRunning = false;
		mStop = true;
	}

	@Override
	public synchronized void start() {
		if(!mRunning && mStop) {
			super.start();
			mRunning = true;
			mStop = false;
		}
	}

	public synchronized void end() {
		if(mRunning) {
			mRunning = false;
		}
	}

	public synchronized void put(Task task) {
		mWeakReferenceTasks.add(new WeakReference<>(task));
	}

	@Override
	public void run() {
		mStop = false;
		while(mRunning) {
			try {
				final WeakReference<Task> taskWF = mWeakReferenceTasks.take();
				final Task task = taskWF.get();
				if(task != null) {
					System.out.println("开始执行: " + task.getClass().getSimpleName());
					task.run();
					mHandler.obtainMessage(0, task).sendToTarget();
				}
			}
			catch(Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}
		mStop = true;
	}

	@Override
	final public boolean handleMessage(Message msg) {
		Task task = (Task) msg.obj;
		task.finish();

		if(mFinishListener != null) {
			mFinishListener.onTaskFinish(task);
		}
		return true;
	}

	public void setOnManagerFinishListener(OnTaskFinishListener listener) {
		mFinishListener = listener;
	}
}
