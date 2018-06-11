package lm.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import lm.context.ContextHelper;

/**
 * Created by limin on 16/01/05.
 */
public abstract class ViewToast extends ContextHelper {
	private final Handler mHandler;

	private final WindowManager mWM;

	private final WindowManager.LayoutParams mParams;

	private View mRootView;

	private boolean isShowing;

	public ViewToast(Context context) {
		super(context);
		mHandler = new Handler(Looper.myLooper());
		mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mRootView = onCreateView(LayoutInflater.from(getContext()));
		mParams = new WindowManager.LayoutParams();
		onViewCreated(mRootView);
	}

	protected abstract View onCreateView(LayoutInflater inflater);

	protected void onViewCreated(View view) {
	}

	protected void onConfigAttributes(WindowManager.LayoutParams attrs) {
	}

	public int getDuration() {
		return 2000;
	}

	protected void onWillShow() {
	}

	protected void onShow() {
	}

	protected void onWillHide() {
	}

	protected void onHide() {
	}

	private Runnable mShowRunnable = new Runnable() {
		@Override
		public void run() {
			mWM.addView(mRootView, mParams);
			onShow();
		}
	};

	private Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mWM.removeView(mRootView);
			onHide();
		}
	};

	private Runnable mCancelTask = new Runnable() {
		@Override
		public void run() {
			hide();
		}
	};

	final public void show() {
		show(true);
	}

	final public void show(boolean auto) {
		synchronized(this) {
			if(!isShowing) {
				onConfigAttributes(mParams);
				onWillShow();
				mHandler.post(mShowRunnable);
				isShowing = true;
				if(auto) {
					mHandler.postDelayed(mCancelTask, getDuration());
				}
			}
		}
	}

	final public void hide() {
		synchronized(this) {
			if(isShowing) {
				onWillHide();
				mHandler.post(mHideRunnable);
				isShowing = false;
			}
		}
	}

	public boolean isShowing() {
		return isShowing;
	}
}
