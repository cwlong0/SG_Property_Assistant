package com.softgrid.shortvideo.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.customView.CImageView;
import com.softgrid.shortvideo.customView.FlowLayout;
import com.softgrid.shortvideo.info.Bridge;
import com.softgrid.shortvideo.model.Bespoke;
import com.softgrid.shortvideo.model.User;
import com.softgrid.shortvideo.utils.AppHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianfeng on 2017/8/25.
 */

public class BespokeAdapter extends BaseAdapter {


    private Context context;
    private List<Bespoke> itemList;


    public BespokeAdapter(Context context){
        this.context = context;
        itemList = new ArrayList<Bespoke>();
    }

    public void setItemsData(ArrayList<Bespoke> dataList){
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
    public Bespoke getItem(int position) {

        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView != null){
            viewHolder = (ViewHolder)convertView.getTag();
        }
        else{
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_bespoke, null);
            viewHolder.iconImage = convertView.findViewById(R.id.building_image);
            viewHolder.typeImage = convertView.findViewById(R.id.building_type_bg);
            viewHolder.typeText = convertView.findViewById(R.id.building_type_text);
            viewHolder.titleText = convertView.findViewById(R.id.building_title);
            viewHolder.descText = convertView.findViewById(R.id.building_desc);
            viewHolder.totalPriceText = convertView.findViewById(R.id.building_total_text);
            viewHolder.unitPriceText = convertView.findViewById(R.id.building_unit_text);
            viewHolder.userIcon = convertView.findViewById(R.id.user_icon);
            viewHolder.userName = convertView.findViewById(R.id.user_name);
            viewHolder.userCompany = convertView.findViewById(R.id.user_company);
            viewHolder.besTime = convertView.findViewById(R.id.bes_time_text);

            convertView.setTag(viewHolder);
        }

        Bespoke model = getItem(position);

        if (model.getBuilding().getThumbnail() != null){
            Glide.with(context.getApplicationContext())
                    .load(Uri.parse(model.getBuilding().getThumbnail()))
                    .apply(AppHelper.getInstance().getImageOptions())
                    .into(viewHolder.iconImage);
        }

        viewHolder.titleText.setText(model.getBuilding().getTitle());
        String descText = "";
        descText = model.getBuilding().getRooms() + context.getString(R.string.room_unit);
        descText = descText +  " | "  + model.getBuilding().getArea() + context.getString(R.string.area_unit);
        if (model.getBuilding().getDecorate() > 0){
            descText = descText + " | " +
                    AppHelper.getInstance().getDecorate(context, model.getBuilding().getDecorate());
        }
        if (model.getBuilding().getOrientation() > 0){
            descText = descText + " | " +
                    AppHelper.getInstance().getOrientation(context, model.getBuilding().getOrientation());
        }
        viewHolder.descText.setText(descText);

        if (model.getBuilding().getIsResale() > 0){  //二手房
            viewHolder.typeImage.setVisibility(View.GONE);
            viewHolder.typeText.setVisibility(View.GONE);

            viewHolder.unitPriceText.setVisibility(View.VISIBLE);
            viewHolder.totalPriceText.setText(AppHelper.getInstance().formatPrice(context,
                    model.getBuilding().getTotalPrice(), ""));
            viewHolder.unitPriceText.setText(AppHelper.getInstance().formatPrice(context,
                    model.getBuilding().getUnitPrice(), context.getString(R.string.price_unit_1)));

        }
        else {  //新房
            viewHolder.totalPriceText.setText(AppHelper.getInstance().formatPrice(context,
                    model.getBuilding().getUnitPrice(), context.getString(R.string.price_unit_1)));
            viewHolder.unitPriceText.setVisibility(View.GONE);
            viewHolder.typeImage.setVisibility(View.VISIBLE);
            viewHolder.typeText.setVisibility(View.VISIBLE);

        }

        if (model.getIntermediary() != null){
            if (model.getIntermediary().getImage() != null){
                Glide.with(context.getApplicationContext())
                        .load(Uri.parse(model.getIntermediary().getImage()))
                        .apply(AppHelper.getInstance().getUserOptions())
                        .into(viewHolder.userIcon);
            }
            viewHolder.userName.setText(model.getIntermediary().getName());
            viewHolder.userCompany.setText(model.getIntermediary().getCompany());
        }

        viewHolder.besTime.setText(AppHelper.getInstance().
                stampToDateString(model.getBespokeTime(), "yyyy-MM-dd"));

        return convertView;
    }

    static class ViewHolder{

        ImageView iconImage;
        ImageView typeImage;
        TextView typeText;
        TextView titleText;
        TextView descText;
        TextView totalPriceText;
        TextView unitPriceText;

        ImageView userIcon;
        TextView userName;
        TextView userCompany;
        TextView besTime;

    }

}
