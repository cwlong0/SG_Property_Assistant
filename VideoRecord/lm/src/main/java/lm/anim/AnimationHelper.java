package lm.anim;

import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

/**
 * Created by limin on 2016-01-22.
 */
public abstract class AnimationHelper {
	private boolean mFinished;

	private int mDuration;

	private long mStartTime;

	private float mDurationReciprocal;

	private Interpolator mInterpolator;

	public AnimationHelper(Interpolator interpolator) {
		mInterpolator = interpolator;
		mFinished = true;
	}

	final public boolean isFinish() {
		return mFinished;
	}

	final public int getDuration() {
		return mDuration;
	}

	final public boolean computeScrollOffset() {
		if(mFinished) {
			return false;
		}

		int timePassed = (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);

		float interpolator;
		if(timePassed < mDuration) {
			interpolator = mInterpolator == null ? timePassed * mDurationReciprocal :
					mInterpolator.getInterpolation(timePassed * mDurationReciprocal);
		}
		else {
			mFinished = true;
			interpolator = 1;
		}

		onComputeScrollOffset(interpolator);

		return true;
	}


	protected abstract void onComputeScrollOffset(float interpolator);

	final protected void startAnimation(int duration) {
		mDuration = duration;
		mStartTime = AnimationUtils.currentAnimationTimeMillis();
		mFinished = false;
		mDurationReciprocal = 1.0f / (float) mDuration;
	}

	final public void forceFinished(boolean finished) {
		mFinished = finished;
	}
}