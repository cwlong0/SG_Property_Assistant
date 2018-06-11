package lm.kit.lazy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import lm.kit.annotation.BindLayout;
import lm.kit.annotation.FindView;
import lm.kit.annotation.FindViewArray;

/**
 * Created by limin on 2016/06/30.
 */
public class LazyKit {
	public static void bind(Activity activity) {
		BindLayout layout = getBindLayout(activity);

		if(layout != null) {
			bindActivity(activity, layout);
		}
	}

	public static void bind(Object target, View view) {
		bindTargetField(view, target);
	}

	public static View inflater(Object target, LayoutInflater inflater, ViewGroup container) {
		BindLayout layout = getBindLayout(target);
		if(layout != null) {
			View view = inflater.inflate(layout.layout(), container, false);
			if(target != null) {
				bindTargetField(view, target);
			}
			return view;
		}

		return null;
	}

	public static View inflater(Object target, ViewGroup container) {
		if(container != null) {
			return inflater(target, LayoutInflater.from(container.getContext()), container);
		}

		return null;
	}

	public static View inflater(Object target, Context context) {
		return inflater(target, LayoutInflater.from(context), null);
	}

	public static View inflaterClass(Class<?> target, ViewGroup container) {
		if(target == null) {
			return null;
		}

		BindLayout layout = target.getAnnotation(BindLayout.class);

		if(layout != null && container != null) {
			LayoutInflater inflater = LayoutInflater.from(container.getContext());
			return inflater.inflate(layout.layout(), container, false);
		}

		return null;
	}

	public static View inflater(Context context) {
		return inflater(null, context);
	}

	private static BindLayout getBindLayout(Object object) {
		Class<?> cls = object.getClass();
		return cls.getAnnotation(BindLayout.class);
	}

	private static void bindActivity(Activity activity, BindLayout layout) {
		if(activity.getActionBar() != null && layout.actionBar() != -1) {
			activity.getActionBar().setCustomView(layout.actionBar());
		}
		activity.setContentView(layout.layout());
		bindActivityField(activity);
	}

	private static void bindActivityField(Activity activity) {
		try {
			for(Field field : activity.getClass().getDeclaredFields()) {

				FindView findView = field.getAnnotation(FindView.class);
				if(findView != null) {
					View v = activity.findViewById(findView.id());
					field.setAccessible(true);
					field.set(activity, v);
					continue;
				}

				FindViewArray findViewArray = field.getAnnotation(FindViewArray.class);
				if(findViewArray != null && field.getType().isArray()) {
					int[] ids = findViewArray.ids();

					Class<?> cls = field.getType().getComponentType();
					Object[] objects = (Object[]) Array.newInstance(cls, ids.length);

					for(int index = 0; index < ids.length; index++) {
						objects[index] = activity.findViewById(ids[index]);
					}

					field.setAccessible(true);
					field.set(activity, objects);
				}
			}
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private static void bindTargetField(View view, Object target) {
		try {
			for(Field field : target.getClass().getDeclaredFields()) {

				FindView findView = field.getAnnotation(FindView.class);
				if(findView != null) {
					View v = view.findViewById(findView.id());
					field.setAccessible(true);
					field.set(target, v);
					continue;
				}

				FindViewArray findViewArray = field.getAnnotation(FindViewArray.class);
				if(findViewArray != null && field.getType().isArray()) {
					int[] ids = findViewArray.ids();

					Class<?> cls = field.getType().getComponentType();
					Object[] objects = (Object[]) Array.newInstance(cls, ids.length);

					for(int index = 0; index < ids.length; index++) {
						objects[index] = view.findViewById(ids[index]);
					}

					field.setAccessible(true);
					field.set(target, objects);
				}
			}
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
