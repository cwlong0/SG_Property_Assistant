package com.softgrid.shortvideo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;

/**
 * ImageLoader配置
 * 
 * @ClassName: ImageLoaderOptions.java
 * 
 * @author Xun.Zhang
 * 
 * @date 2014-12-8 下午12:59:10
 */
public class ImageLoaderUtil {
	private static String TAG = "ImageLoader";
	/**
	 * ImageLoader配置参数
	 */
	private static final int MAX_IMAGE_WIDTH = 480; // 480px
	private static final int MAX_IMAGE_HEIGHT = 800; // 800px
	private static final int MAX_IMAGE_MEMORY_CACHE_SIZE = 2 * 1024 * 1024; // 2MB
	private static final int MAX_IMAGE_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
	private static final int MAX_IMAGE_DISK_FILE_COUNT = 500;
	/**
	 * 图片圆角像素
	 */
	private static final int IMAGE_CORNER_RADIUS_PIXELS = 150;
	public static DisplayImageOptions DefaultDispOption = null;

	/**
	 * 初始化ImageLoader(只在Application的OnCreate中调用一次就行，其他时候不调)
	 * 
	 * @param applicationContext
	 */
	public static void initImageLoader(Context applicationContext) {

		// ImageLoader内存缓存最大为系统可用内存数的1/8，默认为2MB
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int maxImageMemoryCacheSize = (maxMemory == 0) ? ImageLoaderUtil.MAX_IMAGE_MEMORY_CACHE_SIZE : (maxMemory / 8);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(applicationContext).memoryCache(new LruMemoryCache(maxImageMemoryCacheSize))
				.memoryCacheExtraOptions(ImageLoaderUtil.MAX_IMAGE_WIDTH, ImageLoaderUtil.MAX_IMAGE_HEIGHT).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().diskCacheSize(ImageLoaderUtil.MAX_IMAGE_DISK_CACHE_SIZE)
				.diskCacheFileCount(ImageLoaderUtil.MAX_IMAGE_DISK_FILE_COUNT).diskCacheFileNameGenerator(new Md5FileNameGenerator())
				//.writeDebugLogs()
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);
		Log.d(TAG,"imageloader init");
		DefaultDispOption =  new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).build();
	}

	/**
	 * 清楚ImageLoader的缓存
	 */
	public static void clearCache() {
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().clearDiskCache();
	}

	/**
	 * 显示常规的图片
	 * 
	 * @param imageResId
	 *            默认图片资源ID
	 * @return DisplayImageOptions
	 */
	public static DisplayImageOptions getDisplayImageOptions(int imageResId) {
		return getDisplayImageOptions(imageResId, false, true);
	}

	public static void displayImage() {

	}

	/**
	 * 显示圆形的图片
	 * 
	 * @param imageResId
	 *            默认图片资源ID
	 * @return DisplayImageOptions
	 */
	public static DisplayImageOptions getRoundDisplayImageOptions(int imageResId) {
		return getDisplayImageOptions(imageResId, true, true);
	}

	/**
	 * 不显示加载中的默认图片
	 * 
	 * @return DisplayImageOptions
	 */
	public static DisplayImageOptions getNotShowDisplayImageOptions() {
		return getDisplayImageOptions(0, false, true);
	}

	/**
	 * @param imageResId
	 *            图片资源ID
	 * @param cornerRadiusPixels
	 *            显示图片是否为圆形
	 * @param resetViewBeforeLoading
	 *            是否需要加载前重置视图
	 * @return DisplayImageOptions
	 */
	private static DisplayImageOptions getDisplayImageOptions(int imageResId, boolean cornerRadiusPixels, boolean resetViewBeforeLoading) {
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		if (0 != imageResId) {
			builder.showImageOnLoading(imageResId);
			builder.showImageForEmptyUri(imageResId);
			builder.showImageForEmptyUri(imageResId);
		}
		builder.cacheInMemory(true);
		builder.cacheOnDisk(true);
		builder.bitmapConfig(Bitmap.Config.RGB_565);
		builder.imageScaleType(ImageScaleType.EXACTLY);
		if (cornerRadiusPixels) {
			builder.displayer(new RoundedBitmapDisplayer(IMAGE_CORNER_RADIUS_PIXELS));
		}
		builder.resetViewBeforeLoading(resetViewBeforeLoading);
		return builder.build();
	}

	private static DisplayImageOptions getDisplayImageOptions(int imageResId, int cornerRadiusPixels, boolean resetViewBeforeLoading) {
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		if (0 != imageResId) {
			builder.showImageOnLoading(imageResId);
			builder.showImageForEmptyUri(imageResId);
			builder.showImageForEmptyUri(imageResId);
		}
		builder.cacheInMemory(true);
		builder.cacheOnDisk(true);
		builder.bitmapConfig(Bitmap.Config.RGB_565);
		builder.imageScaleType(ImageScaleType.EXACTLY);
		if (cornerRadiusPixels!=0) {
			builder.displayer(new RoundedBitmapDisplayer(cornerRadiusPixels));
		}
		builder.resetViewBeforeLoading(resetViewBeforeLoading);
		return builder.build();
	}

	public static DisplayImageOptions getRoundDisplayImageOptions(int imageResId, int cornerRadiusPixels ) {
		return getDisplayImageOptions(imageResId, cornerRadiusPixels, true);
	}

	public static String getUriFromLocalFile(File file) {
		return Uri.decode(Uri.fromFile(file).toString());
	}

	public static void display(String path, ImageView mImageView) {
		ImageLoader.getInstance().displayImage(path , mImageView);
	}

	public static DisplayImageOptions getRoundImageOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)//设置下载的图片是否缓存在内存中
				.cacheOnDisk(true)
				.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
				.displayer(new RoundedBitmapDisplayer(13))//是否设置为圆角，弧度为多少
//      .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
				.build();//构建完成

		return options;
	}
}
