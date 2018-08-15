package lm.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import lm.R;

/**
 * Created by limin on 15/11/12.
 */
public class PickerTextView extends View implements GestureDetector.OnGestureListener {
    private final static int DEFAULT_TEXT_COLOR = Color.BLACK;

    final String TAG = "PickerTextView";
    VelocityTracker travker;

    private final float mDensity = getResources().getDisplayMetrics().density;
    private final ViewConfiguration configuration = ViewConfiguration.get(getContext());
    private final int mTouchSlop = configuration.getScaledTouchSlop();
    private final Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Matrix mMatrix = new Matrix();
    private final Matrix mTempMatrix = new Matrix();
    private final Camera mCamera = new Camera();
    private final RectF mDrawTempRect = new RectF();
    private final RectF mDrawRect = new RectF();
    private final RectF mCenterRect = new RectF();
    private final RectF mBounds = new RectF();

    private ColorStateList mTextColor;

    private int mFocusColor;

    private int mDefaultColor;

    private int mItemHeight;

    private Drawable mDrawable;

    private int mCount;

    private float mFontAscent, mFontDescent;

    private int mWidth, mHeight;

    private int mDegreePreItem;

    private int mRadius;

    private int mDistancePerDegree;

    private float mDegreeScroll;

    private Adapter mAdapter;

    private String[] mData;

    private int mPosition;

    private int mSavePosition;

    private GestureDetector mDetector;

    public static abstract class Adapter {
        private final DataSetObservable observable = new DataSetObservable();

        private void registerObserver(DataSetObserver observer) {
            observable.registerObserver(observer);
        }

        private void unregisterObserver(DataSetObserver observer) {
            observable.unregisterObserver(observer);
        }

        public void notifyChanged() {
            observable.notifyChanged();
        }

        public abstract int getCount();

        public abstract String getString(int position);
    }

    private OnPositionChangeListener mPositionChangeListener;

    public interface OnPositionChangeListener {
        void onPositionChange(PickerTextView sender, int position);
    }

    private static final int TOUCH_NONE = 0;
    private static final int TOUCH_SCROLL = 1;
    private static final int TOUCH_NORMAL = 2;

    private int mTouch;

    private ScrollerFloat mValueAnimation;

    private int mVelocityY;

    private int mTouchDownX, mTouchDownY;

    public PickerTextView(Context context) {
        this(context, null);
    }

    public PickerTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PickerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ColorStateList textColor = null;
        float textSize = (int) (mDensity * 12);
        int itemHeight = (int) (mDensity * 20);
        Drawable focusBackground = null;
        int count = 3;

        if (attrs != null) {
            TypedArray ta = getResources().obtainAttributes(attrs, R.styleable.PickerTextView);

            textColor = ta.getColorStateList(
                    R.styleable.PickerTextView_lm_text_color);

            textSize = ta.getDimension(
                    R.styleable.PickerTextView_lm_text_size, mDensity * 12);

            itemHeight = ta.getDimensionPixelSize(
                    R.styleable.PickerTextView_lm_item_height, (int) (mDensity * 20));

            count = ta.getInteger(
                    R.styleable.PickerTextView_lm_show_count, 3);

            focusBackground = ta.getDrawable(
                    R.styleable.PickerTextView_lm_focus_background);

            ta.recycle();
        }

        setTextColor(textColor);
        setTextSize(textSize);
        setItemHeight(itemHeight);
        setFocusBackground(focusBackground);
        setShowCount(count);

        initPickerView();
    }

    private void initPickerView() {
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFilterBitmap(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mDetector = new GestureDetector(getContext(), this);

        mValueAnimation = new ScrollerFloat(getContext(), new DecelerateInterpolator());

        if (isInEditMode()) {
            Adapter adapter = new Adapter() {
                @Override
                public int getCount() {
                    return mCount;
                }

                @Override
                public String getString(int position) {
                    return "Item " + position;
                }
            };

            setAdapter(adapter);
        }
    }

    public void setTextSize(float size) {
        mTextPaint.setTextSize(size);

        final Paint.FontMetrics fm = mTextPaint.getFontMetrics();

        mFontAscent = fm.ascent;
        mFontDescent = fm.descent;

        if (isLayoutRequested()) {
            invalidate();
        }
    }

    public void setTextColor(ColorStateList colors) {
        if (colors == null) {
            colors = ColorStateList.valueOf(DEFAULT_TEXT_COLOR);
        }

        mTextColor = colors;

        mFocusColor = mTextColor.getColorForState(new int[]{android.R.attr.state_focused},
                DEFAULT_TEXT_COLOR);

        mDefaultColor = mTextColor.getDefaultColor();

        if (isLayoutRequested()) {
            invalidate();
        }
    }

    public void setTextColor(int color) {
        setTextColor(ColorStateList.valueOf(color));
    }

    public void setShowCount(int count) {
//        if (count < 3) count = 3;
        mCount = count;

        if (isLayoutRequested()) {
            requestLayout();
        }

        if (isInEditMode()) {
            Adapter adapter = new Adapter() {
                @Override
                public int getCount() {
                    return mCount;
                }

                @Override
                public String getString(int position) {
                    return "Item " + position;
                }
            };

            setAdapter(adapter);
        }
    }

    public void setItemHeight(int height) {
        mItemHeight = height;

        if (isLayoutRequested()) {
            requestLayout();
        }
    }

    public void setFocusBackground(Drawable focusBackground) {
        if (isLayoutRequested() && focusBackground != null) {
            focusBackground.setBounds(
                    (int) mCenterRect.left,
                    (int) mCenterRect.top,
                    (int) mCenterRect.right,
                    (int) mCenterRect.bottom);
            focusBackground.invalidateSelf();
        }

        mDrawable = focusBackground;
    }

    public void setPosition(int position) {
        if (position < 0) {
            position = 0;
        }

        mPosition = position;

//		if(isLayoutRequested()) {
        mDegreeScroll = mPosition * mDegreePreItem;
//		}

        invalidate();
    }

    private final DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            final Adapter adapter = mAdapter;

            String[] data = null;
            if (adapter != null) {
                data = new String[adapter.getCount()];
            }
            mData = data;

            invalidate();
        }
    };

    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterObserver(mObserver);
        }

        if (adapter != null) {
            adapter.registerObserver(mObserver);
        }

        mAdapter = adapter;
        mPosition = 0;
        mObserver.onChanged();
    }

    public void setOnPositionChangeListener(OnPositionChangeListener listener) {
        mPositionChangeListener = listener;
    }

    public int getPosition() {
        if (mData == null) {
            return -1;
        }

        return mPosition;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);

        mDegreePreItem = 90 / (mCount + 1);
        mRadius = (int) (mItemHeight * 0.5 / Math.tan(Math.toRadians(mDegreePreItem) * 0.5));
        mDistancePerDegree = mRadius / 90;

        mHeight = mRadius * 2 + getPaddingTop() + getPaddingBottom();

        mBounds.set(getPaddingLeft(), getPaddingTop(),
                mWidth - getPaddingRight(), mHeight - getPaddingBottom());

        mCenterRect.set(getPaddingLeft(), 0, mWidth - getPaddingRight(), mItemHeight);
        mCenterRect.offset(0, mBounds.centerY() - mCenterRect.centerY());

        if (mDrawable != null) {
            mDrawable.setBounds(
                    (int) mCenterRect.left,
                    (int) mCenterRect.top,
                    (int) mCenterRect.right,
                    (int) mCenterRect.bottom);
        }

        mDegreeScroll = mPosition * mDegreePreItem;

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mDetector.addTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float startValue, finalValue;

        final int touchX = (int) event.getX();
        final int touchY = (int) event.getY();
        getTracker(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = touchX;
                mTouchDownY = touchY;

                mTouch = TOUCH_NONE;
                mSavePosition = mPosition;
                mValueAnimation.forceFinished(true);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mTouch != TOUCH_NONE) {
                    break;
                }

                if (Math.abs(mTouchDownX - touchX) > mTouchSlop) {
                    mTouch = TOUCH_NORMAL;
                    break;
                }

                if (Math.abs(mTouchDownY - touchY) > mTouchSlop) {
                    mTouch = TOUCH_SCROLL;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                if (mTouch == TOUCH_SCROLL) {
                    startValue = mDegreeScroll;
                    finalValue = mSavePosition * mDegreePreItem;
                    mValueAnimation.start(startValue, finalValue);
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                travker.computeCurrentVelocity(1000);
                int velocity = (int) travker.getYVelocity();
                int duration = 300;
                if (Math.abs(velocity) >= 1000) {
                    int size = (velocity - 1000) / 250;
                    int tempPosition = mPosition;
                    mPosition -= size;
                    mPosition = mPosition < 0 ? 0 : mPosition >= mData.length ? mData.length - 1 : mPosition;
                    duration = duration + 30 * (Math.abs(mPosition - tempPosition));
                }
                startValue = mDegreeScroll;
                finalValue = mPosition * mDegreePreItem;
                mValueAnimation.start(startValue, finalValue, duration);
                invalidate();
                releaseTracker();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mValueAnimation.computeAnimation()) {
            mDegreeScroll = mValueAnimation.getCurr();
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final Drawable drawable = mDrawable;
        if (drawable != null) {
            drawable.draw(canvas);
        }

        final String[] data = mData;
        final Adapter adapter = mAdapter;

        if (data == null || adapter == null) {
            return;
        }

        final RectF rect = mDrawRect;
        rect.set(0, 0, mWidth, mItemHeight);

        final RectF center = mCenterRect;

        final float scroll = mDegreeScroll;

        float start;
        int startPosition;

        if (scroll <= 90) {
            start = scroll;
            startPosition = 0;
        } else {
            float offset = scroll - 90;

            startPosition = ((int) offset % mDegreePreItem) == 0 ? (int) offset / mDegreePreItem :
                    (int) offset / mDegreePreItem + 1;
            start = scroll - startPosition * mDegreePreItem;
        }

        int position = (int) Math.floor(mDegreeScroll / mDegreePreItem + 0.5);
        position = position < 0 ? 0 : position >= data.length ? data.length - 1 : position;
        if (mPosition != position) {
            mPosition = position;

            if (mPositionChangeListener != null) {
                mPositionChangeListener.onPositionChange(this, position);
            }
        }

        for (float degree = start; degree >= -90; degree -= mDegreePreItem, startPosition++) {
            if (startPosition >= data.length) {
                break;
            }

            String name = data[startPosition];
            if (name == null) {
                data[startPosition] = adapter.getString(startPosition);
                name = data[startPosition];
            }

            float radians = (float) Math.toRadians(degree);
            float pos = (float) (mBounds.centerY() - Math.sin(radians) * mRadius);

            float alpha = (float) Math.cos(radians);
            alpha = alpha < 0 ? 0 : alpha * alpha * alpha + 0.2f;
            alpha = alpha > 1 ? 1 : alpha;

            rect.offset(0, pos - rect.centerY());

            getMatrix(rect, radians);

            mDrawTempRect.set(rect);
            if (mDrawTempRect.intersect(center)) {
                canvas.save();
                canvas.clipRect(mDrawTempRect);

                mTextPaint.setColor(mFocusColor);

                canvas.drawText(name,
                        rect.centerX(),
                        rect.centerY() - ((mFontAscent + mFontDescent) * 0.5f),
                        mTextPaint);
                canvas.restore();

                // 非交集部分
                mDrawTempRect.set(rect);
                if (rect.top < center.top) {
                    mDrawTempRect.bottom = center.top;
                } else {
                    mDrawTempRect.top = center.bottom;
                }
            }

            if (!mDrawTempRect.isEmpty()) {
                canvas.save();
                canvas.clipRect(mDrawTempRect);
                canvas.concat(mMatrix);

                mTextPaint.setColor(mDefaultColor);
                mTextPaint.setAlpha((int) (0xFF * alpha));

                canvas.drawText(name,
                        rect.centerX(),
                        rect.centerY() - ((mFontAscent + mFontDescent) * 0.5f),
                        mTextPaint);
                canvas.restore();
            }
        }
    }

    private void getMatrix(RectF rect, float radians) {
        float delta = (float) Math.cos(radians);

        mCamera.save();
        mCamera.translate(0, 0, mItemHeight * (1 - delta) * 0.5f + mItemHeight);
        mCamera.rotateX((float) Math.toDegrees(radians));
        mMatrix.reset();
        mCamera.getMatrix(mMatrix);
        mCamera.restore();

        mMatrix.preTranslate(-rect.centerX(), -rect.centerY());
        mMatrix.postTranslate(rect.centerX(), rect.centerY());
    }

    @Override
    public void onScrollGesture(float distanceX, float distanceY) {

        if (mTouch == TOUCH_SCROLL) {
            mDegreeScroll += distanceY / mDistancePerDegree;
            invalidate();
        }
    }

    @Override
    public void onFlingGesture(float velocityX, float velocityY) {
        mVelocityY = (int) velocityY;
    }


    void getTracker(MotionEvent event) {
        if (travker == null) {
            travker = VelocityTracker.obtain();
        }
        travker.addMovement(event);
    }

    void releaseTracker() {
        if (travker != null) {
            travker.clear();
            travker.recycle();
            travker = null;
        }
    }
}