package lm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import lm.R;

/**
 * Created by limin on 16/01/05.
 */
public class HorizontalLayout extends ViewGroup {
	private LayoutManager mLayoutManager;

	private int mVerticalSpacing;

	private int mHorizontalSpacing;

	public HorizontalLayout(Context context) {
		super(context);
		initHorizontalLayout();
	}

	public HorizontalLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public HorizontalLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		int verticalSpacing = 0;
		int horizontalSpacing = 0;

		if(attrs != null) {
			TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HorizontalLayout);

			verticalSpacing = ta.getDimensionPixelSize(R.styleable.HorizontalLayout_verticalSpacing, 0);
			horizontalSpacing = ta.getDimensionPixelSize(R.styleable.HorizontalLayout_horizontalSpacing, 0);

			ta.recycle();
		}

		mVerticalSpacing = verticalSpacing;
		mHorizontalSpacing = horizontalSpacing;

		initHorizontalLayout();
	}

	private void initHorizontalLayout() {
		mLayoutManager = new LayoutManager();
	}

	@Override
	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
		super.onInitializeAccessibilityNodeInfo(info);
		info.setClassName(getClass().getSimpleName());
	}

	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
		super.onInitializeAccessibilityEvent(event);
		event.setClassName(getClass().getSimpleName());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		mLayoutManager.reset(width, height, mHorizontalSpacing, mVerticalSpacing);

		int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

		for(int index = 0; index < getChildCount(); index++) {
			View child = getChildAt(index);

			if(child.getVisibility() != GONE) {
				LayoutParams params = (LayoutParams) child.getLayoutParams();
				params.width = params.width < 0 ? LayoutParams.WRAP_CONTENT : params.width;
				params.height = params.height < 0 ? LayoutParams.WRAP_CONTENT : params.height;
				child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
				mLayoutManager.put(child);
			}
		}

		super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
				mLayoutManager.getHeight(), MeasureSpec.EXACTLY));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mLayoutManager.layout();
	}

	@Override
	protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	static class Row {
		public int width, height;
		public int y;
	}

	static class ViewNode {
		public View view;

		public int width, height;

		public Row row;

		public int x;

		ViewNode next;
	}

	static class LayoutManager {
		private int mWidth;
		private int mHeight;

		private int mHorizontalSpacing;
		private int mVerticalSpacing;

		private ViewNode mFirst;
		private ViewNode mLast;

		public void put(View view) {
			ViewNode node = new ViewNode();
			node.view = view;

			LayoutParams params = (LayoutParams) view.getLayoutParams();
			node.width = view.getMeasuredWidth() + params.leftMargin + params.rightMargin;
			node.height = view.getMeasuredHeight() + params.topMargin + params.bottomMargin;

			if(mFirst == null) {
				node.x = 0;

				node.row = new Row();

				node.row.y = 0;
				node.row.width = node.width;
				node.row.height = node.height;

				mFirst = node;
				mLast = node;
			}
			else {
				if(mLast.row.width + node.width + mHorizontalSpacing > mWidth) {
					node.row = new Row();
					node.row.y = mLast.row.y + mLast.row.height + mVerticalSpacing;
					node.x = 0;
				}
				else {
					node.row = mLast.row;
					node.x = mLast.x + mLast.width + mHorizontalSpacing;
				}

				node.row.height = Math.max(node.row.height, node.height);
				node.row.width += node.width;

				mLast.next = node;

				mLast = mLast.next;
			}
		}

		public void reset(int width, int height, int horizontalSpacing, int verticalSpacing) {
			mWidth = width;
			mHeight = height;

			mHorizontalSpacing = horizontalSpacing;
			mVerticalSpacing = verticalSpacing;

			mFirst = null;
			mLast = null;
		}

		public int getHeight() {
			if(mLast == null) {
				return mHeight;
			}

			return mLast.row.y + mLast.row.height;
		}

		public void layout() {
			ViewNode node = mFirst;

			while(node != null) {
				if(node.view.getVisibility() == VISIBLE) {
					int lm, tm, rm, bm;
					LayoutParams params = (LayoutParams) node.view.getLayoutParams();
					lm = params.leftMargin;
					tm = params.topMargin;
					rm = params.rightMargin;
					bm = params.bottomMargin;

					int left = node.x;
					int top = (int) (node.row.y + (node.row.height - node.height) * 0.5f);
					int right = left + node.width;
					int bottom = top + node.height;

					left += lm;
					top += tm;
					right -= rm;
					bottom -= bm;

					node.view.layout(left, top, right, bottom);
				}

				node = node.next;
			}
		}
	}

	public static class LayoutParams extends MarginLayoutParams {
		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
		}

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}

		public LayoutParams(MarginLayoutParams source) {
			super(source);
		}
	}
}
