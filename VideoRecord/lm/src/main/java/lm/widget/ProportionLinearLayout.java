package lm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import lm.R;

/**
 * Created by limin on 15/11/18.
 */
public class ProportionLinearLayout extends LinearLayout {
	public enum Relative {
		NONE,
		WIDTH,
		HEIGHT
	}

	private Relative mRelative;
	private float mProportion;
	private int mMaxWidth, mMaxHeight;

	public ProportionLinearLayout(Context context) {
		this(context, null);
	}

	public ProportionLinearLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ProportionLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		int proportionRelative = 0;
		float proportion = 0;
		int maxWidth = -1;
		int maxHeight = -1;

		if(attrs != null) {
			TypedArray ta = getResources().obtainAttributes(attrs,
					R.styleable.ProportionLinearLayout);

			proportionRelative = ta.getInteger(
					R.styleable.ProportionLinearLayout_lm_proportion_relative, 0);
			proportion = ta.getFloat(
					R.styleable.ProportionLinearLayout_lm_proportion, 0);
			maxWidth = ta.getDimensionPixelSize(
					R.styleable.ProportionLinearLayout_lm_max_width, -1);
			maxHeight = ta.getDimensionPixelSize(
					R.styleable.ProportionLinearLayout_lm_max_height, -1);

			ta.recycle();
		}

		setRelativeInteger(proportionRelative);
		setProportion(proportion);
		setMaxWidth(maxWidth);
		setMaxHeight(maxHeight);
	}

	private void setProportion(float proportion) {
		mProportion = proportion;
		if(mRelative != Relative.NONE) {
			requestLayout();
		}
	}

	private void setRelativeInteger(int relative) {
		switch(relative) {
		case 0:
			setRelative(Relative.NONE);
			break;

		case 1:
			setRelative(Relative.WIDTH);
			break;

		case 2:
			setRelative(Relative.HEIGHT);
			break;

		default:
			break;
		}
	}

	public void setRelative(Relative relative) {
		mRelative = relative;
		requestLayout();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		if(mRelative == Relative.WIDTH) {
			if(width > mMaxWidth && mMaxWidth != -1) {
				widthMeasureSpec = MeasureSpec.makeMeasureSpec(
						mMaxWidth, MeasureSpec.EXACTLY);

				width = mMaxWidth;
			}

			super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
					(int) (width * mProportion), MeasureSpec.EXACTLY));
		}
		else if(mRelative == Relative.HEIGHT) {
			if(height > mMaxHeight && mMaxHeight != -1) {
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(
						mMaxHeight, MeasureSpec.EXACTLY);

				height = mMaxHeight;
			}

			super.onMeasure(MeasureSpec.makeMeasureSpec(
					(int) (height * mProportion), MeasureSpec.EXACTLY), heightMeasureSpec);
		}
		else {
			if(width > mMaxWidth && mMaxWidth != -1) {
				widthMeasureSpec = MeasureSpec.makeMeasureSpec(
						mMaxWidth, MeasureSpec.EXACTLY);
			}

			if(height > mMaxHeight && mMaxHeight != -1) {
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(
						mMaxHeight, MeasureSpec.EXACTLY);
			}

			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	public void setMaxWidth(int maxWidth) {
		mMaxWidth = maxWidth;
		requestLayout();
	}

	public void setMaxHeight(int maxHeight) {
		mMaxHeight = maxHeight;
		requestLayout();
	}
}
