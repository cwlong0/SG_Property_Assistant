package lm.widget;

import android.content.Context;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

/**
 * Created by limin on 15/11/17.
 */
public class ScrollerFloat {
    private float mStart;
    private float mEnd;
    private float mCurr;

    private long mStartTime;

    private int mDuration;

    private boolean mFinished;

    private float mDurationReciprocal;

    private Interpolator mInterpolator;

    public ScrollerFloat(Context context) {
        this(context, null);
    }

    public ScrollerFloat(Context context, Interpolator interpolator) {
        mInterpolator = interpolator;
        mFinished = true;
    }

    public boolean computeAnimation() {
        if (mFinished) {
            return false;
        }

        int timePassed = (int) (AnimationUtils.currentAnimationTimeMillis() - mStartTime);

        if (timePassed < mDuration) {
            float a = timePassed * mDurationReciprocal;
            if (mInterpolator != null)
                a = mInterpolator.getInterpolation(a);
            mCurr = a * (mEnd - mStart) + mStart;
        } else {
            mCurr = mEnd;
            mFinished = true;
        }

        return true;
    }

    public boolean isFinish() {
        return mFinished;
    }

    public float getStart() {
        return mStart;
    }

    public float getEnd() {
        return mEnd;
    }

    public float getCurr() {
        return mCurr;
    }

    public void start(float start, float end) {
        start(start, end, 300);
    }

    public void start(float start, float end, int duration) {
        mStart = start;
        mCurr = mStart;
        mEnd = end;
        mDuration = duration;
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        mFinished = false;
        mDurationReciprocal = 1.0f / (float) mDuration;
    }

    public final void forceFinished(boolean finished) {
        mFinished = finished;
    }

}