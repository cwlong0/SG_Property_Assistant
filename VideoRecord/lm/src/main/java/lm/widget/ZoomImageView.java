package lm.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import lm.anim.AnimationHelper;

/**
 * Created by limin on 15/12/12.
 */
public class ZoomImageView extends ImageView implements Handler.Callback {
	private final static float SCALE_MAX = 2.5f;
	private final static float SCALE_MIN = 1.0f;

	private final static int SINGLE_TAP = 1;

	private final Matrix mMatrix = new Matrix();

	private final float[] mMatrixValues = new float[9];

	private final Matrix mChangeMatrix = new Matrix();

	private GestureDetector mGestureDetector;
	private ScaleGestureDetector mScaleGestureDetector;
	private OnLongClickListener mLongClickListener;
	private OnClickListener mClickListener;

	private Handler mHandler;

	@Override
	public boolean handleMessage(Message msg) {
		if(msg.what == SINGLE_TAP && mClickListener != null) {
			mClickListener.onClick(ZoomImageView.this);
		}
		return true;
	}

	enum Mode {
		NONE,
		DRAG,
		LONG_PRESS,
		DOUBLE_TAP,
		ZOOM
	}

	private Mode mMode;

	private float mFocusX, mFocusY;

	private float mInitialScale;

	private float mWidth, mHeight;

	private float mContentWidth, mContentHeight;

	private ScaleAnimation mAnimation;

	private Runnable mComputeScale = new Runnable() {
		@Override
		public void run() {
			computeScale();
		}
	};

	public ZoomImageView(Context context) {
		this(context, null);
	}

	public ZoomImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initZoomImageView();
	}

	private void initZoomImageView() {
		super.setScaleType(ScaleType.MATRIX);
		mGestureDetector = new GestureDetector(getContext(), new DoubleTapGesture());
		mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGesture());
		mAnimation = new ScaleAnimation(new DecelerateInterpolator());
		mHandler = new Handler(this);
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
	}

	@Override
	public void setImageResource(int resId) {
		Drawable drawable = getResources().getDrawable(resId);
		setImageDrawable(drawable);
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		mChangeMatrix.reset();
		computeInitialMatrix();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = MeasureSpec.getSize(widthMeasureSpec);
		mHeight = MeasureSpec.getSize(heightMeasureSpec);
		computeInitialMatrix();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final Drawable drawable = getDrawable();
		if(drawable == null) {
			return false;
		}

		final int action = event.getActionMasked();

		switch(action) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(!canParentIntercept());
			mMode = Mode.NONE;

			mGestureDetector.onTouchEvent(event);
			mScaleGestureDetector.onTouchEvent(event);
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mGestureDetector.onTouchEvent(event);
			mScaleGestureDetector.onTouchEvent(event);
			if(mMode != Mode.LONG_PRESS && mMode != Mode.DOUBLE_TAP) {
				final float scale = getScale();
				if(scale < SCALE_MIN) {
					mAnimation.start(scale, SCALE_MIN);
				}

				if(scale > SCALE_MAX) {
					mAnimation.start(scale, SCALE_MAX);
				}
			}
			break;

		default:
			mGestureDetector.onTouchEvent(event);
			mScaleGestureDetector.onTouchEvent(event);
			break;
		}

		return true;
	}

	private void computeInitialMatrix() {
		final Drawable drawable = getDrawable();

		if(drawable != null) {
			final int drawableWidth = drawable.getIntrinsicWidth();
			final int drawableHeight = drawable.getIntrinsicHeight();
			mInitialScale = Math.min(mWidth / drawableWidth, mHeight / drawableHeight);

			mContentWidth = drawableWidth * mInitialScale;
			mContentHeight = drawableHeight * mInitialScale;

			adjustAndSetMatrixToImageView();
		}
	}

	/**
	 * 修正位置
	 */
	private void adjustAndSetMatrixToImageView() {
		if(getDrawable() != null) {
			mChangeMatrix.getValues(mMatrixValues);

			final float newWidth = mMatrixValues[Matrix.MSCALE_X] * mContentWidth;
			final float newHeight = mMatrixValues[Matrix.MSCALE_Y] * mContentHeight;

			final float transX = mMatrixValues[Matrix.MTRANS_X];
			final float transY = mMatrixValues[Matrix.MTRANS_Y];

			float newTransX;
			if(newWidth >= mWidth) {
				float left = transX;
				if(left > 0) {
					left = 0;
				}

				float right = left + newWidth;
				if(right < mWidth) {
					right = mWidth;
					left = right - newWidth;
				}

				newTransX = left;
			}
			else {
				newTransX = (mWidth - newWidth) * 0.5f;
			}

			float newTransY;
			if(newHeight >= mHeight) {
				float top = transY;
				if(top > 0) {
					top = 0;
				}

				float bottom = top + newHeight;
				if(bottom < mHeight) {
					bottom = mHeight;
					top = bottom - newHeight;
				}

				newTransY = top;
			}
			else {
				newTransY = (mHeight - newHeight) * 0.5f;
			}

			mChangeMatrix.postTranslate(newTransX - transX, newTransY - transY);

			mMatrix.reset();
			mMatrix.setScale(mInitialScale, mInitialScale);
			mMatrix.postConcat(mChangeMatrix);
			setImageMatrix(mMatrix);
		}
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		mLongClickListener = l;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		mClickListener = l;
	}

	private float getScale() {
		float[] values = new float[9];
		mChangeMatrix.getValues(values);
		return values[Matrix.MSCALE_X];
	}

	private void scaleBy(float ds) {
		mChangeMatrix.postScale(ds, ds, mFocusX, mFocusY);
		adjustAndSetMatrixToImageView();
		post(mComputeScale);
	}

	private void scaleTo(float scale) {
		float s = getScale();
		scaleBy(scale / s);
	}

	private void computeScale() {
		if(mAnimation.computeScrollOffset()) {
			final float scale = mAnimation.getCurrScale();
			scaleTo(scale);
		}
	}

	class DoubleTapGesture extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			if(mHandler.hasMessages(SINGLE_TAP)) {
				mHandler.removeMessages(SINGLE_TAP);
			}
			return true;

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			if(mMode == Mode.NONE && mClickListener != null) {
				mHandler.sendEmptyMessageDelayed(SINGLE_TAP, ViewConfiguration.getDoubleTapTimeout());
			}
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			super.onLongPress(e);
			mMode = Mode.LONG_PRESS;
			if(mLongClickListener != null) {
				mLongClickListener.onLongClick(ZoomImageView.this);
			}
		}

		@Override
		public boolean onDoubleTap(MotionEvent event) {
			mMode = Mode.DOUBLE_TAP;

			final float scale = getScale();

			mFocusX = event.getX();
			mFocusY = event.getY();

			if(scale < 1.75f) {
				mAnimation.start(scale, SCALE_MAX);
			}
			else {
				mAnimation.start(scale, SCALE_MIN);
			}

			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			if(mMode == Mode.DRAG) {
				mChangeMatrix.postTranslate(-distanceX, -distanceY);
				adjustAndSetMatrixToImageView();
			}

			if(mMode == Mode.NONE) {
				mMode = Mode.DRAG;
			}
			return true;
		}
	}

	private boolean canParentIntercept() {
		if(getDrawable() != null) {
			mChangeMatrix.getValues(mMatrixValues);

			int newRight = (int) (mMatrixValues[Matrix.MTRANS_X] +
					(mContentWidth * mMatrixValues[Matrix.MSCALE_X]));

			return mMatrixValues[Matrix.MTRANS_X] >= 0 && newRight <= mWidth;
		}
		return true;
	}

	class ScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			if(mMode == Mode.NONE || mMode == Mode.DRAG) {
				mMode = Mode.ZOOM;
			}
			return true;
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			if(mMode == Mode.ZOOM) {
				final float scale = detector.getScaleFactor();
				mFocusX = detector.getFocusX();
				mFocusY = detector.getFocusY();
				scaleBy(scale);
			}
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			if(mMode == Mode.ZOOM) {
				mMode = Mode.DRAG;
			}
		}
	}

	class ScaleAnimation extends AnimationHelper {
		private float mFromScale;
		private float mToScale;
		private float mCurrScale;

		public ScaleAnimation(Interpolator interpolator) {
			super(interpolator);
		}

		@Override
		protected void onComputeScrollOffset(float interpolator) {
			mCurrScale = (mToScale - mFromScale) * interpolator + mFromScale;
		}

		public float getCurrScale() {
			return mCurrScale;
		}

		public void start(float from, float to) {
			start(from, to, 300);
		}

		public void start(float from, float to, int duration) {
			mFromScale = from;
			mToScale = to;
			mCurrScale = mFromScale;
			startAnimation(duration);
			scaleTo(mCurrScale);
		}
	}
}