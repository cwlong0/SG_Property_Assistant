package lm.context;

import android.app.Activity;

/**
 * Created by limin on 2016-02-23.
 */
public class ActivityHelper extends ContextHelper {
	private final Activity activity;

	public ActivityHelper(Activity activity) {
		super(activity);
		this.activity = activity;
	}

	public Activity getActivity() {
		return activity;
	}

	public static ActivityHelper from(Activity activity) {
		return new ActivityHelper(activity);
	}
}
