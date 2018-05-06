package com.softgrid.shortvideo;

import android.app.Application;

import com.softgrid.shortvideo.utils.CrashHandler;
import com.softgrid.shortvideo.utils.ImageLoaderUtil;

/**
 * Created by Raymond Pen on 2018/4/10.
 */


public class VideoApp extends Application {
    @Override
    public  void  onCreate() {
        super.onCreate();
        ImageLoaderUtil.initImageLoader(this);
        CrashHandler.getInstance().init(this);
    }
}

