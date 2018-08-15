package lm.context;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by limin on 16/01/13.
 */
@SuppressWarnings("unused")
public class ContextHelper {
	private Context context;
	private Resources resources;
	private DisplayMetrics displayMetrics;
	private AssetManager assetManager;

	public ContextHelper(Context context) {
		this.context = context;
		this.resources = context.getResources();
		this.displayMetrics = this.resources.getDisplayMetrics();
		this.assetManager = context.getAssets();
	}

	final public Context getContext() {
		return this.context;
	}

	final public DisplayMetrics getDisplayMetrics() {
		return displayMetrics;
	}

	final public float getDensity() {
		return displayMetrics.density;
	}

	final public int getWidthPixels() {
		return displayMetrics.widthPixels;
	}

	final public int getHeightPixels() {
		return displayMetrics.heightPixels;
	}

	final public String getString(int id) {
		return resources.getString(id);
	}

	final public String[] getStringArray(int id) {
		return resources.getStringArray(id);
	}

	final public float getDimension(int id) {
		return resources.getDimension(id);
	}

	final public int getDimensionPixelOffset(int id) {
		return resources.getDimensionPixelOffset(id);
	}

	final public int getDimensionPixelSize(int id) {
		return resources.getDimensionPixelSize(id);
	}

	final public int getColor(int id) {
		return resources.getColor(id);
	}

	final public AssetManager getAssetManager() {
		return assetManager;
	}

	final public InputStream openAsset(String fileName) throws IOException {
		return assetManager.open(fileName);
	}

	final public Object getSystemService(String name) {
		return context.getSystemService(name);
	}
}
