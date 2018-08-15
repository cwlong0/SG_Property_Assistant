package com.softgrid.shortvideo.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.PopupWindow;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.tool.DeviceInfoTool;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tianfeng on 2017/8/29.
 */

public class AppHelper {

    private static AppHelper info;

    private RequestOptions userOptions;
    private RequestOptions imageOptions;
    private RequestOptions bannerOptions;

    private AppHelper(){


        imageOptions = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.fc_realestate_placeholder)
                .error(R.drawable.fc_realestate_placeholder)
                .priority(Priority.HIGH);

        userOptions = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.fc_boy_headdefault)
                .error(R.drawable.fc_boy_headdefault)
                .priority(Priority.HIGH);

        bannerOptions = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.fc_banner_placeholder)
                .error(R.drawable.fc_banner_placeholder)
                .priority(Priority.HIGH);

    }

    public static AppHelper getInstance() {
        if (info == null) {
            info = new AppHelper();
        }
        return info;
    }

    public RequestOptions getUserOptions(){
        return userOptions;
    }

    public RequestOptions getImageOptions(){
        return imageOptions;
    }

    public RequestOptions getBannerOptions() {
        return bannerOptions;
    }

    public String formatPrice(Context context, float price, String unit){
        String sPrice = "";
        if (price < 100000){
            sPrice = "" + String.format("%.0f", price) + unit;
        }
        else if(price >= 100000){
            float wan = price/10000.f;
            sPrice = String.format("%.0f", wan) + context.getString(R.string.wan) + unit;
        }
        return sPrice;
    }

    public String stampToDateString(long timeStamp, String format){

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String dateString = dateFormat.format(new Date(timeStamp));
        return dateString;
    }

    public long dateStringToStamp(String dateString, String format){

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            Date date = dateFormat.parse(dateString);
            long time = date.getTime();
            return time;
        }
        catch (Exception e){
            return 0;
        }

    }

    /**根据状态获取状态描述*/
    public String getStatus(Context context, int status) {
        String statusStr = "";
        switch (status){
            case 1:
                statusStr = context.getString(R.string.status_1);
                break;

            case 2:
                statusStr = context.getString(R.string.status_2);
                break;

            case 3:
                statusStr = context.getString(R.string.status_3);
                break;

            case 4:
                statusStr = context.getString(R.string.status_4);
                break;

        }
        return statusStr;
    }

    /**根据装修类型获取装修描述*/
    public String getDecorate(Context context, int type){
        String decorate = "";
        switch (type){
            case 1:
                decorate = context.getString(R.string.decorate_1);
                break;

            case 2:
                decorate = context.getString(R.string.decorate_2);
                break;

            case 3:
                decorate = context.getString(R.string.decorate_3);
                break;

            case 4:
                decorate = context.getString(R.string.decorate_4);
                break;

            case 5:
                decorate = context.getString(R.string.decorate_5);
                break;

        }
        return decorate;
    }

    /**根据朝向类型获取朝向描述*/
    public String getOrientation(Context context, int orientation){
        String orientationStr = "";
        switch (orientation){
            case 1:
                orientationStr = context.getString(R.string.orientation_1);
                break;

            case 2:
                orientationStr = context.getString(R.string.orientation_2);
                break;

            case 3:
                orientationStr = context.getString(R.string.orientation_3);
                break;

            case 4:
                orientationStr = context.getString(R.string.orientation_4);
                break;

            case 5:
                orientationStr = context.getString(R.string.orientation_5);
                break;

            case 6:
                orientationStr = context.getString(R.string.orientation_6);
                break;

            case 7:
                orientationStr = context.getString(R.string.orientation_7);
                break;

            case 8:
                orientationStr = context.getString(R.string.orientation_8);
                break;

        }
        return orientationStr;
    }

    public String getRootPath(Context context){
        String path = Environment.getExternalStorageDirectory() + "/FC/" + DeviceInfoTool.getPageName(context) + "/temp";
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        return path;
    }

    /**
     * popupWindow菜单弹出动画
     *
     * @param view 做出动画的view
     *
     * @param popupWindow 菜单
     *
     * @param show  显示或者隐藏
     *
     * */
    public void startAnimation(Activity activity, View view, final PopupWindow popupWindow, final boolean show) {
        int start = 0;
        int end = 0;
        if (show) {
            start = DeviceInfoTool.getScreenHeight(activity);
        } else {
            end = DeviceInfoTool.getScreenHeight(activity);
        }
        ObjectAnimator startAnimation = ObjectAnimator.ofFloat(view, "translationY", start, end);
        startAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!show) {
                    popupWindow.dismiss();

                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.play(startAnimation);
        set.start();
    }
}
