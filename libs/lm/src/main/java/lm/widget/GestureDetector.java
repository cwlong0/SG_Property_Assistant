package lm.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/**
 * Created by limin on 15/10/19.
 */
public class GestureDetector {

	/**
	 *
	 */
	public interface OnGestureListener {
		/**
		 *
		 * @param distanceX
		 * @param distanceY
		 */
		void onScrollGesture(float distanceX, float distanceY);

		/**
		 *
		 * @param velocityX
		 * @param velocityY
		 */
		void onFlingGesture(float velocityX, float velocityY);
	}

	private OnGestureListener mListener;

	private float mLastFocusX;
	private float mLastFocusY;

	private int mMinimumFlingVelocity, mMaximumFlingVelocity;

	private VelocityTracker mVelocityTracker;

	public GestureDetector(Context context, OnGestureListener listener) {
		mListener = listener;
		ViewConfiguration configuration = ViewConfiguration.get(context);

		mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
		mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
	}

	public void addTouchEvent(MotionEvent event) {
		final boolean pointerUp = event.getActionMasked() == MotionEvent.ACTION_POINTER_UP;
		final int skipIndex = pointerUp ? event.getActionIndex() : -1;

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);

		// Determine focal point
		float sumX = 0, sumY = 0;
		final int count = event.getPointerCount();
		for(int i = 0; i < count; i++) {
			if (skipIndex == i) continue;

			sumX += event.getX(i);
			sumY += event.getY(i);
		}
		final int div = pointerUp ? count - 1 : count;
		final float focusX = sumX / div;
		final float focusY = sumY / div;


		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_DOWN:
			mLastFocusX = focusX;
			mLastFocusY = focusY;
			break;

		case MotionEvent.ACTION_POINTER_UP:
			mLastFocusX = focusX;
			mLastFocusY = focusY;

			// Check the dot product of current velocities.
			// If the pointer that left was opposing another velocity vector, clear.
			mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
			final int upIndex = event.getActionIndex();
			final int id1 = event.getPointerId(upIndex);
			final float x1 = mVelocityTracker.getXVelocity(id1);
			final float y1 = mVelocityTracker.getYVelocity(id1);
			for (int i = 0; i < count; i++) {
				if (i == upIndex) continue;

				final int id2 = event.getPointerId(i);
				final float x = x1 * mVelocityTracker.getXVelocity(id2);
				final float y = y1 * mVelocityTracker.getYVelocity(id2);

				final float dot = x + y;
				if (dot < 0) {
					mVelocityTracker.clear();
					break;
				}
			}
			break;

		case MotionEvent.ACTION_MOVE:
			final float scrollX = mLastFocusX - focusX;
			final float scrollY = mLastFocusY - focusY;

			if(mListener != null) {
				mListener.onScrollGesture(scrollX, scrollY);
			}

			mLastFocusX = focusX;
			mLastFocusY = focusY;
			break;

		case MotionEvent.ACTION_CANCEL:
			if(mVelocityTracker != null) {
				// This may have been cleared when we called out to the
				// application above.
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			break;

		case MotionEvent.ACTION_UP:
			// A fling must travel the minimum tap distance
			final VelocityTracker velocityTracker = mVelocityTracker;
			final int pointerId = event.getPointerId(0);
			velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
			final float velocityY = velocityTracker.getYVelocity(pointerId);
			final float velocityX = velocityTracker.getXVelocity(pointerId);

			if(mListener != null) {
				mListener.onFlingGesture(velocityX, velocityY);
			}

			if(mVelocityTracker != null) {
				// This may have been cleared when we called out to the
				// application above.
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			break;
		}
	}
}
