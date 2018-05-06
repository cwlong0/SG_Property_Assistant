package com.softgrid.shortvideo.upload;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by Raymond Pen on 2017/11/17.
 */

public class VideoListPersistence {
    private SharedPreferences sharedPreferences;
    private static final String VIDEO_PATH = "VIDEO_PATH";

    public VideoListPersistence(Context context) {
        sharedPreferences = context.getSharedPreferences("video_persistent_data", Activity.MODE_PRIVATE);
    }

    public String getVideo() {
        return sharedPreferences.getString(VIDEO_PATH, "");
    }

    public void saveVideo(String path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(VIDEO_PATH, path);
        editor.commit();
    }
}
