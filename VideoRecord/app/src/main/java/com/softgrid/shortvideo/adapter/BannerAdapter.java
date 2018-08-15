package com.softgrid.shortvideo.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.customView.CImageView;
import com.softgrid.shortvideo.model.Banner;
import com.softgrid.shortvideo.utils.AppHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tianfeng on 2017/8/26.
 */

public class BannerAdapter extends PagerAdapter {

    private Context context;
    private ViewPager viewPager;
    private List<Banner> dataList;

    private int currentPos;

    private boolean touch;

    private Handler handler;

    public BannerAdapter(Context context, ViewPager viewPager){
        this.context = context;
        this.dataList = new ArrayList<>();
        this.viewPager = viewPager;

        currentPos = 0;
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int pos = currentPos + 1;
                if (pos > dataList.size() - 1){
                    pos = 0;
                }
                if (!touch){
                    BannerAdapter.this.viewPager.setCurrentItem(pos);
                }
                timeUpdate();
            }
        };

        this.timeUpdate();
    }

    public void setData(List<Banner> dataList){
        this.dataList.clear();
        if (dataList != null){
            this.dataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public void setTouch(boolean touch){
        this.touch = touch;
    }

    public void setCurrentPos(int currentPos){
        this.currentPos = currentPos;
    }

    public int getCurrentPos(){
        return currentPos;
    }

    private void timeUpdate(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 5000);
    }

    @Override
    public int getCount() {

        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        // TODO Auto-generated method stub
        return view == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        CImageView imageView = new CImageView(context);
        imageView.setImageResource(R.drawable.fc_banner_placeholder);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                Banner banner = dataList.get(position);
//                if (banner.getBannerContentType() == 1){ //用户
//                    Announcer announcer = new Announcer();
//                    announcer.setAnnouncerId(banner.getBannerUid());
//                    XMApi.getInstance().getAnnouncerDetail(context, announcer, new CallBackListener() {
//                        @Override
//                        public void onComplete(HashMap<String, Object> values) {
//                            Bridge.getInstance().showXMAnchor(context,
//                                    (Announcer) values.get(Constant.DATA_KEY));
//                        }
//
//                        @Override
//                        public void onError(ErrorInfo error) {
//                            Toast.makeText(context, R.string.error_unkown, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    Bridge.getInstance().showXMAnchor(context, announcer);
//                }

            }
        });
        container.addView(imageView);

        Banner banner = dataList.get(position);

        Glide.with(context.getApplicationContext())
                .load(Uri.parse(banner.getBannerUrl()))
                .apply(AppHelper.getInstance().getBannerOptions())
                .into(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}
