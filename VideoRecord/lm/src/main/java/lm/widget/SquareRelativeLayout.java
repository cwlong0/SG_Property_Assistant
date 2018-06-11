package lm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import lm.R;

/**
 * Created by limin on 15/12/16.
 */
public class SquareRelativeLayout extends RelativeLayout {
	private int relative;

	public SquareRelativeLayout(Context context) {
		this(context, null);
	}

	public SquareRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		relative = 0;
		if(attrs != null) {
			TypedArray ta = getResources().obtainAttributes(attrs, R.styleable
					.SquareRelativeLayout);
			relative = ta.getInteger(R.styleable.SquareRelativeLayout_lm_relative, 0);
			ta.recycle();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		if(relative == 0) {
			height = MeasureSpec.getSize(widthMeasureSpec);
		}
		else {
			width = MeasureSpec.getSize(heightMeasureSpec);
		}

		super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
	}
}
