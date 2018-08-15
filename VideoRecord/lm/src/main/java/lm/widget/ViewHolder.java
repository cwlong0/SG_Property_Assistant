package lm.widget;

import android.view.View;
import android.view.ViewGroup;

import lm.R;

/**
 * Created by limin on 2016/07/22.
 */
public abstract class ViewHolder<T> {
	private final View mItemView;

	public ViewHolder(ViewGroup parent) {
		mItemView = onCreateItemView(parent);
		onViewCreated();
		mItemView.setTag(R.id.TAG_VIEW_HOLDER, this);
	}

	protected void onViewCreated() {
	}

	public final View getView() {
		return mItemView;
	}

	public final void bind(T model, int position) {
		onBind(model, position);
	}

	protected abstract void onBind(T model, int position);

	protected abstract View onCreateItemView(ViewGroup parent);

	public static ViewHolder from(View itemView) {
		Object object = itemView.getTag(R.id.TAG_VIEW_HOLDER);
		if(object != null && object instanceof ViewHolder) {
			return (ViewHolder) object;
		}

		return null;
	}
}
