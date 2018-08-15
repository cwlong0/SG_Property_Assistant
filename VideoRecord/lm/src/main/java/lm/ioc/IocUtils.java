package lm.ioc;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lm.ioc.annotaion.FragmentId;
import lm.ioc.annotaion.Ids;
import lm.ioc.annotaion.Layout;
import lm.ioc.annotaion.Id;
import lm.ioc.annotaion.LayoutWithActionBar;

/**
 * Created by limin on 15/12/03.
 */
public class IocUtils {

	public static View inject(LayoutInflater inflater, ViewGroup container, Object target) {
		Class<?> clazz = target.getClass();
		Layout layout = clazz.getAnnotation(Layout.class);

		if(layout != null) {
			int layoutResId = layout.value();
			View view = inflater.inflate(layoutResId, container, false);
			injectViews(target, view);
			return view;
		}

		return null;
	}

	public static View inject(LayoutInflater inflater, Object target) {
		return inject(inflater, null, target);
	}

	public static View inject(Context context, Object target) {
		return inject(LayoutInflater.from(context), target);
	}

	public static View inject(Context context, ViewGroup container, Object target) {
		return inject(LayoutInflater.from(context), container, target);
	}

	public static View injectFragment(LayoutInflater inflater, ViewGroup container, Fragment fragment) {
		Class<?> clazz = fragment.getClass();
		Layout layout = clazz.getAnnotation(Layout.class);

		if(layout != null) {
			View view = inflater.inflate(layout.value(), container, false);
			injectViews(fragment, view);

			Field[] fields = clazz.getDeclaredFields();
			for(Field field : fields) {
				FragmentId fragmentId = field.getAnnotation(FragmentId.class);

				try {
					if(fragmentId != null) {
						Fragment value = null;

						if(Build.VERSION.SDK_INT > 17) {
							value = fragment.getChildFragmentManager()
									.findFragmentById(fragmentId.value());
						}

						if(value == null) {
							value = fragment.getFragmentManager()
									.findFragmentById(fragmentId.value());
						}
						if(value != null) {
							field.setAccessible(true);
							field.set(fragment, value);
						}
					}
				}
				catch(IllegalAccessException e) {
					e.printStackTrace();
				}

			}

			return view;
		}

		return null;
	}

	private static void injectViews(Object target, View view) {
		Class<?> clazz = target.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			Id viewId = field.getAnnotation(Id.class);

			if(viewId != null) {
				int id = viewId.value();
				try {
					field.setAccessible(true);
					field.set(target, view.findViewById(id));
				}
				catch(IllegalAccessException e) {
					e.printStackTrace();
				}

				continue;
			}

			Ids viewIds = field.getAnnotation(Ids.class);

			if(viewIds != null) {
				int ids[] = viewIds.value();
				Class<?> cls = field.getType().getComponentType();
				Object[] views = (Object[]) Array.newInstance(cls, ids.length);

				for(int index = 0; index < ids.length; index++) {
					views[index] = view.findViewById(ids[index]);
				}

				try {
					field.setAccessible(true);
					field.set(target, views);
				}
				catch(IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void injectActivity(Activity activity) {
		Class<?> clazz = activity.getClass();
		Layout layout = clazz.getAnnotation(Layout.class);
		LayoutWithActionBar layoutWithActionBar = clazz.getAnnotation(LayoutWithActionBar.class);

		if(layout != null || layoutWithActionBar != null) {
			try {
				int contentValue = layout != null ? layout.value()
						: layoutWithActionBar.contentLayout();

				Method setContentView = clazz.getMethod("setContentView", int.class);
				setContentView.invoke(activity, contentValue);

				Method getActionBar = clazz.getMethod("getActionBar");
				Object actionBar = getActionBar.invoke(activity);
				if(layoutWithActionBar != null && actionBar != null) {
					((ActionBar) actionBar).setCustomView(layoutWithActionBar.actionBar());
				}

				Method findViewById = clazz.getMethod("findViewById", int.class);

				Field[] fields = clazz.getDeclaredFields();
				for(Field field : fields) {
					Id viewId = field.getAnnotation(Id.class);

					if(viewId != null) {
						Object view = findViewById.invoke(activity, viewId.value());
						field.setAccessible(true);
						field.set(activity, view);
						continue;
					}

					Ids viewIds = field.getAnnotation(Ids.class);

					if(viewIds != null) {
						int ids[] = viewIds.value();
						Class<?> cls = field.getType().getComponentType();
						Object[] views = (Object[]) Array.newInstance(cls, ids.length);

						for(int index = 0; index < ids.length; index++) {
							views[index] = findViewById.invoke(activity, ids[index]);
						}

						field.getDeclaringClass();

						field.setAccessible(true);
						field.set(activity, views);
					}
				}

			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void injectDialog(Dialog dialog) {
		Class<?> clazz = dialog.getClass();
		Layout layout = clazz.getAnnotation(Layout.class);

		if(layout != null) {
			try {
				Method setContentView = clazz.getMethod("setContentView", int.class);
				setContentView.invoke(dialog, layout.value());

				Method findViewById = clazz.getMethod("findViewById", int.class);

				Field[] fields = clazz.getDeclaredFields();
				for(Field field : fields) {
					Id viewId = field.getAnnotation(Id.class);

					if(viewId != null) {
						Object view = findViewById.invoke(dialog, viewId.value());
						field.setAccessible(true);
						field.set(dialog, view);
						continue;
					}

					Ids viewIds = field.getAnnotation(Ids.class);

					if(viewIds != null) {
						int ids[] = viewIds.value();
						Class<?> cls = field.getType().getComponentType();
						Object[] views = (Object[]) Array.newInstance(cls, ids.length);

						for(int index = 0; index < ids.length; index++) {
							views[index] = findViewById.invoke(dialog, ids[index]);
						}

						field.getDeclaringClass();

						field.setAccessible(true);
						field.set(dialog, views);
					}
				}

			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
