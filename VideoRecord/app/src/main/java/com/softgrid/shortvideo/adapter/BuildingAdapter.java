package com.softgrid.shortvideo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.customView.CImageView;
import com.softgrid.shortvideo.customView.FlowLayout;
import com.softgrid.shortvideo.model.Building;
import com.softgrid.shortvideo.utils.AppHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianfeng on 2017/8/25.
 */

public class BuildingAdapter extends BaseAdapter {

    private Context context;
    private List<Building> itemList;

    public BuildingAdapter(Context context){
        this.context = context;
        itemList = new ArrayList<Building>();
    }

    public void setItemsData(ArrayList<Building> dataList){
        itemList.clear();
        if (dataList != null){
            itemList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return itemList.size();
    }

    @Override
    public Building getItem(int position) {

        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        BuildingViewHolder viewHolder;
        if (convertView != null && convertView.getTag() instanceof BuildingViewHolder){
            viewHolder = (BuildingViewHolder)convertView.getTag();
        }
        else{
            viewHolder = new BuildingViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_building, null);
            viewHolder.invalidView = convertView.findViewById(R.id.building_invalid_layout);
            viewHolder.deleteView = convertView.findViewById(R.id.building_invalid_delete);
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

        Building building = getItem(position);

        if (building.isValid()){
            viewHolder.invalidView.setVisibility(View.GONE);
            viewHolder.totalPriceText.setTextColor(context.getColor(R.color.main_top));
            viewHolder.titleText.setTextColor(context.getColor(R.color.color_text));
        }
        else {
            viewHolder.invalidView.setVisibility(View.VISIBLE);
            viewHolder.totalPriceText.setTextColor(context.getColor(R.color.color_detail));
            viewHolder.titleText.setTextColor(context.getColor(R.color.color_detail));
        }

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

    static class BuildingViewHolder{

        View invalidView;
        View deleteView;

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

}
