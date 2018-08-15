package lm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import lm.R;

/**
 * Created by limin on 15/11/18.
 */
public class ProportionImageView extends ImageView {
	public enum Relative {
		NONE,
		WIDTH,
		HEIGHT
	}

	private Relative mRelative;
	private float mProportion;

	public ProportionImageView(Context context) {
		this(context, null);
	}

	public ProportionImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ProportionImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		int proportionRelative = 0;
		float proportion = 0;

		if(attrs != null) {
			TypedArray ta = getResources().obtainAttributes(attrs,
					R.styleable.ProportionLinearLayout);

			proportionRelative = ta.getInteger(
					R.styleable.ProportionLinearLayout_lm_proportion_relative, 0);
			proportion = ta.getFloat(
					R.styleable.ProportionLinearLayout_lm_proportion, 0);

			ta.recycle();
		}

		setRelativeInteger(proportionRelative);
		setProportion(proportion);
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
			super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
					(int) (width * mProportion), MeasureSpec.EXACTLY));
		}
		else if(mRelative == Relative.HEIGHT) {
			super.onMeasure(MeasureSpec.makeMeasureSpec(
					(int) (height * mProportion), MeasureSpec.EXACTLY), heightMeasureSpec);
		}
		else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}
