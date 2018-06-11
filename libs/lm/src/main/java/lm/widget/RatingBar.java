package lm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import lm.R;

/**
 * Created by limin on 15/10/26.
 */
public class RatingBar extends View {
	private Drawable mRatingDrawable;

	private int mW, mH;

	private int mRatingMax;

	private int mRating;

	private Rect[] mBounds;

	private Rect mSelfBound;

	public interface OnRatingChangeListener {
		void OnRatingChange(RatingBar sender, int rating);
	}

	private OnRatingChangeListener mRatingChangeListener;

	public RatingBar(Context context) {
		super(context);
		initRatingBar();
	}

	public RatingBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, 0);

		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);

		mRatingDrawable = ta.getDrawable(R.styleable.RatingBar_lm_rating_src);
		mRatingMax = ta.getInt(R.styleable.RatingBar_lm_rating_max, 5);

		ta.recycle();

		initRatingBar();
	}

	private void initRatingBar() {
		setBackgroundColor(Color.TRANSPARENT);
	}

	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
		super.onInitializeAccessibilityEvent(event);
		event.setClassName(RatingBar.class.getName());
	}

	@Override
	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
		super.onInitializeAccessibilityNodeInfo(info);
		info.setClassName(RatingBar.class.getName());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mW = MeasureSpec.getSize(widthMeasureSpec);
		mH = MeasureSpec.getSize(heightMeasureSpec);

		int left = getPaddingLeft();
		int top = getPaddingTop();
		int right = mW - getPaddingRight();
		int bottom = mH - getPaddingBottom();

		mSelfBound = new Rect(left, top, right, bottom);

		int size = bottom - top;
		int unit = (right - left) / mRatingMax;

		Rect cell = new Rect(0, 0, unit, size);

		mBounds = new Rect[mRatingMax];
		for(int i = 0; i < mBounds.length; i++) {
			int newLeft = left + i * cell.width();
			int newTop = top;
			cell.offsetTo(newLeft, newTop);

			mBounds[i] = new Rect(cell);
			mBounds[i].inset((cell.width() - cell.height()) / 2, 0);
		}
	}

	private final int[] mValid = {android.R.attr.state_selected};
	private final int[] mInvalid = {};

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if(mRatingDrawable == null) return;

		final Drawable drawable = mRatingDrawable;
		final Rect[] bounds = mBounds;

		canvas.save();

		for(int i = 0; i < bounds.length; i++) {
			drawable.setState(mRating == 0 ? mInvalid : i <= (mRating - 1) ? mValid : mInvalid);

			drawable.setBounds(bounds[i]);
			drawable.draw(canvas);
		}

		canvas.restore();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final Rect self = mSelfBound;

		int x = (int) event.getX();
		int y = (int) event.getY();

		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			return self.contains(x, y);

		case MotionEvent.ACTION_UP:
			int index = (x - self.left) * mRatingMax / self.width();
			int rating = mRating;
			if(index == 0) {
				rating = rating == 0 ? 1 : 0;
			}
			else {
				rating = rating == index + 1 ? 0 : index + 1;
			}

			if(mRating != rating) {
				mRating = rating;
				invalidate();
				// 监听
				if(mRatingChangeListener != null) {
					mRatingChangeListener.OnRatingChange(this, mRating);
				}
			}
			break;
		}

		return true;
	}

	public void setRating(int rating) {
		if(rating >= 0 && rating <= mRatingMax) {
			mRating = rating;
			invalidate();
		}
	}

	public int getRating() {
		return mRating;
	}

	public void setOnRatingChangeListener(OnRatingChangeListener listener) {
		mRatingChangeListener = listener;
	}
}
