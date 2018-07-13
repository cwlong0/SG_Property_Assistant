package com.softgrid.shortvideo.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.customView.CImageView;
import com.softgrid.shortvideo.customView.FlowLayout;
import com.softgrid.shortvideo.utils.AppHelper;
import com.softgrid.shortvideo.model.Banner;
import com.softgrid.shortvideo.model.Building;
import com.softgrid.shortvideo.tool.DeviceInfoTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianfeng on 2017/8/25.
 */

public class HomeRecommendAdapter extends BaseAdapter {

    public static final int TYPE_TOP = 0;                   //类型:顶部分类与轮播推荐
    public static final int TYPE_TITLE = 1;                 //类型:类型标题
    public static final int TYPE_ITEM = 2;                  //类型:数据

    private Context context;
    private List<Banner> bannerList;
    private String title;
    private List<Building> itemList;

    private BannerAdapter bannerAdapter;

    private ClickItemListener clickItemListener;

    public void setClickItemListener(ClickItemListener clickItemListener) {
        this.clickItemListener = clickItemListener;
    }

    public HomeRecommendAdapter(Context context){
        this.context = context;
        bannerList = new ArrayList<>();
        title = "";
        itemList = new ArrayList<Building>();
    }

    public void setBannerData(ArrayList<Banner> dataList){
        bannerList.clear();
        if (dataList != null){
            bannerList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public void setTitleData(String title){
        this.title = title;
        notifyDataSetChanged();
    }

    public void setItemsData(ArrayList<Building> dataList){
        itemList.clear();
        if (dataList != null){
            itemList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_TOP;
        }
        else if(position == 1){
            return TYPE_TITLE;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getCount() {

        if (title != null && !title.equals("")){
            return 2 + itemList.size();
        }
        else{
            return 1 + itemList.size();
        }
    }

    @Override
    public Object getItem(int position) {

        if (position == 0){
            return bannerList;
        }
        else if(position == 1 && title != null && !title.equals("")){
            return title;
        }

        Building dataItem;
        if (title != null && !title.equals("")){
            dataItem = itemList.get(position - 2);
        }
        else {
            dataItem = itemList.get(position - 1);
        }
        return dataItem;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (getItemViewType(position) == TYPE_TOP){
            final BannerViewHolder viewHolder;
            if (convertView != null && convertView.getTag() instanceof BannerViewHolder){
                viewHolder = (BannerViewHolder)convertView.getTag();
            }
            else{

                viewHolder = new BannerViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.cell_recommend_home_top, null);
                viewHolder.discoveryLayout = convertView.findViewById(R.id.discovery_layout);
                viewHolder.searchLayout = convertView.findViewById(R.id.search_layout);
                viewHolder.searchBtn = convertView.findViewById(R.id.image_search);
                viewHolder.locationBtn = convertView.findViewById(R.id.icon_location);
                viewHolder.bannerPager = convertView.findViewById(R.id.banner_pager);
                viewHolder.bannerNumber = convertView.findViewById(R.id.banner_number);
                viewHolder.buildingNewView = convertView.findViewById(R.id.layout_building_new);
                viewHolder.buildingResaleView = convertView.findViewById(R.id.layout_building_resale);
                viewHolder.consultView = convertView.findViewById(R.id.layout_building_consult);
                viewHolder.qaView = convertView.findViewById(R.id.layout_qa);

                int width = DeviceInfoTool.getScreenWidth((Activity) context);
                int height = width*705/1125 + 1;
                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(width, height);
                viewHolder.discoveryLayout.setLayoutParams(params);


                params = (LinearLayout.LayoutParams)viewHolder.searchLayout.getLayoutParams();
                params.setMargins(0, -((int)(40*DeviceInfoTool.getScreenDensity((Activity) context))), 0 ,0);


                bannerAdapter = new BannerAdapter(context,viewHolder.bannerPager);
                viewHolder.bannerPager.setAdapter(bannerAdapter);

                viewHolder.bannerPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        bannerAdapter.setCurrentPos(position);
                        viewHolder.bannerNumber.setText((bannerAdapter.getCurrentPos() + 1)
                                + "/" + bannerList.size());
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        if (state == 0){
                            bannerAdapter.setTouch(false);
                        }
                        else {
                            bannerAdapter.setTouch(true);
                        }
                    }
                });

                viewHolder.searchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickItemListener != null){
                            clickItemListener.onClick(10);
                        }
                    }
                });

                viewHolder.locationBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickItemListener != null){
                            clickItemListener.onClick(11);
                        }
                    }
                });

                viewHolder.buildingNewView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickItemListener != null){
                            clickItemListener.onClick(1);
                        }
                    }
                });

                viewHolder.buildingResaleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickItemListener != null){
                            clickItemListener.onClick(2);
                        }
                    }
                });

                viewHolder.consultView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickItemListener != null){
                            clickItemListener.onClick(3);
                        }
                    }
                });

                viewHolder.qaView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickItemListener != null){
                            clickItemListener.onClick(4);
                        }
                    }
                });

                convertView.setTag(viewHolder);
            }
            bannerAdapter.setData(bannerList);
            if (bannerList.size() > 1){
                viewHolder.bannerNumber.setVisibility(View.VISIBLE);
                viewHolder.bannerNumber.setText((bannerAdapter.getCurrentPos() + 1)
                        + "/" + bannerList.size());
            }
            else {
                viewHolder.bannerNumber.setVisibility(View.GONE);
            }
            return convertView;
        }
        else if(getItemViewType(position) == TYPE_TITLE){
            TitleViewHolder viewHolder;
            if (convertView != null && convertView.getTag() instanceof TitleViewHolder){
                viewHolder = (TitleViewHolder)convertView.getTag();
            }
            else{
                viewHolder = new TitleViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.cell_recommend_home_title, null);
                viewHolder.titleText = convertView.findViewById(R.id.text_title);

                convertView.setTag(viewHolder);
            }

            viewHolder.titleText.setText(title);

            return convertView;
        }
        else if(getItemViewType(position) == TYPE_ITEM){
            BuildingViewHolder viewHolder;
            if (convertView != null && convertView.getTag() instanceof BuildingViewHolder){
                viewHolder = (BuildingViewHolder)convertView.getTag();
            }
            else{
                viewHolder = new BuildingViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.cell_building, null);
                viewHolder.iconImage = convertView.findViewById(R.id.building_image);
                viewHolder.typeImage = convertView.findViewById(R.id.building_type_bg);
                viewHolder.typeText = convertView.findViewById(R.id.building_type_text);
                viewHolder.statusView = convertView.findViewById(R.id.building_status_layout);
                viewHolder.statusText = convertView.findViewById(R.id.building_status_text);
                viewHolder.titleText = convertView.findViewById(R.id.building_title);
                viewHolder.followerText = convertView.findViewById(R.id.building_follower);
                viewHolder.descText = convertView.findViewById(R.id.building_desc);
                viewHolder.totalPriceText = convertView.findViewById(R.id.building_total_text);
                viewHolder.unitPriceText = convertView.findViewById(R.id.building_unit_text);
                viewHolder.flowLayout = convertView.findViewById(R.id.building_flow);

                convertView.setTag(viewHolder);
            }

            Building building = (Building)getItem(position);

            if (building.getThumbnail() != null){
                Glide.with(context.getApplicationContext())
                        .load(Uri.parse(building.getThumbnail()))
                        .apply(AppHelper.getInstance().getImageOptions())
                        .into(viewHolder.iconImage);
            }

            viewHolder.titleText.setText(building.getTitle());
            viewHolder.followerText.setText(building.getFollowCount() + " "
                    + context.getString(R.string.ren)+ context.getString(R.string.follower));

            if (building.getFeaturesTags() != null && building.getFeaturesTags().size() > 0){
                List<String> tags = new ArrayList<>();
                for (int i = 0; i < building.getFeaturesTags().size(); i++){
                    tags.add(building.getFeaturesTags().get(i).getTitle());
                }
                viewHolder.flowLayout.setFlowLayout(tags, null);
            }
            else {
                List<String> tags = new ArrayList<>();
                viewHolder.flowLayout.setFlowLayout(tags, null);
            }

            String descText = "";
            descText = building.getRooms() + context.getString(R.string.room_unit);
            descText = descText +  " | "  + building.getArea() + context.getString(R.string.area_unit);
            if (building.getDecorate() > 0){
                descText = descText + " | " +
                        AppHelper.getInstance().getDecorate(context, building.getDecorate());
            }
            if (building.getOrientation() > 0){
                descText = descText + " | " +
                        AppHelper.getInstance().getOrientation(context, building.getOrientation());
            }
            viewHolder.descText.setText(descText);
            if (building.getIsResale() > 0){  //二手房
                viewHolder.typeImage.setVisibility(View.GONE);
                viewHolder.typeText.setVisibility(View.GONE);
                viewHolder.statusView.setVisibility(View.GONE);

                viewHolder.unitPriceText.setVisibility(View.VISIBLE);
                viewHolder.totalPriceText.setText(AppHelper.getInstance().formatPrice(context,
                        building.getTotalPrice(), ""));
                viewHolder.unitPriceText.setText(AppHelper.getInstance().formatPrice(context,
                        building.getUnitPrice(), context.getString(R.string.price_unit_1)));

            }
            else {  //新房
                viewHolder.totalPriceText.setText(AppHelper.getInstance().formatPrice(context,
                        building.getUnitPrice(), context.getString(R.string.price_unit_1)));
                viewHolder.unitPriceText.setVisibility(View.GONE);
                viewHolder.typeImage.setVisibility(View.VISIBLE);
                viewHolder.typeText.setVisibility(View.VISIBLE);
                viewHolder.statusView.setVisibility(View.VISIBLE);
                viewHolder.statusText.setText(AppHelper.getInstance().getStatus(context, building.getStatus()));

            }

            return convertView;
        }
        return null;
    }

    static class BannerViewHolder{

        RelativeLayout discoveryLayout;
        RelativeLayout searchLayout;
        CImageView searchBtn;
        CImageView locationBtn;
        ViewPager bannerPager;
        TextView bannerNumber;
        View buildingNewView;
        View buildingResaleView;
        View consultView;
        View qaView;
    }

    static class TitleViewHolder{

        TextView titleText;
    }

    static class BuildingViewHolder{

        ImageView iconImage;
        ImageView typeImage;
        TextView typeText;
        View statusView;
        TextView statusText;
        TextView titleText;
        TextView followerText;
        TextView descText;
        TextView totalPriceText;
        TextView unitPriceText;
        FlowLayout flowLayout;
    }


    public interface ClickItemListener{

        void onClick(int position);
    }
}
