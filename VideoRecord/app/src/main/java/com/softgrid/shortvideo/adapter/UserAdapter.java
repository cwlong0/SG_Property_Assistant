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
import com.softgrid.shortvideo.model.User;
import com.softgrid.shortvideo.utils.AppHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianfeng on 2017/8/25.
 */

public class UserAdapter extends BaseAdapter {


    private Context context;
    private List<User> itemList;


    public UserAdapter(Context context){
        this.context = context;
        itemList = new ArrayList<User>();
    }

    public void setItemsData(ArrayList<User> dataList){
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
    public User getItem(int position) {

        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        UserViewHolder viewHolder;
        if (convertView != null){
            viewHolder = (UserViewHolder)convertView.getTag();
        }
        else{
            viewHolder = new UserViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_user, null);
            viewHolder.iconImage = convertView.findViewById(R.id.user_icon);
            viewHolder.callBtn = convertView.findViewById(R.id.user_callbtn);
            viewHolder.nameText = convertView.findViewById(R.id.user_name);
            viewHolder.starsImage = convertView.findViewById(R.id.user_stars);
            viewHolder.companyText = convertView.findViewById(R.id.user_company);
            viewHolder.chargeText = convertView.findViewById(R.id.user_charge);
            viewHolder.flowLayout = convertView.findViewById(R.id.user_flow);

            convertView.setTag(viewHolder);
        }

        final User user = getItem(position);

        if (user.getImage() != null){
            Glide.with(context.getApplicationContext())
                    .load(Uri.parse(user.getImage()))
                    .apply(AppHelper.getInstance().getImageOptions())
                    .into(viewHolder.iconImage);
        }
        viewHolder.nameText.setText(user.getName());
        viewHolder.companyText.setText(user.getCompany());
        if (user.getCharge() != null){
            viewHolder.chargeText.setVisibility(View.VISIBLE);
            viewHolder.chargeText.setText(user.getCharge());
        }
        else {
            viewHolder.chargeText.setVisibility(View.GONE);
        }

        if (user.getRate() < 20){
            viewHolder.starsImage.setImageResource(R.drawable.fc_stars_01);
        }
        else if (user.getRate() < 30){
            viewHolder.starsImage.setImageResource(R.drawable.fc_stars_02);
        }
        else if (user.getRate() < 40){
            viewHolder.starsImage.setImageResource(R.drawable.fc_stars_03);
        }
        else if (user.getRate() < 50){
            viewHolder.starsImage.setImageResource(R.drawable.fc_stars_04);
        }
        else {
            viewHolder.starsImage.setImageResource(R.drawable.fc_stars_05);
        }

        if (user.getTags() != null && user.getTags().size() > 0){
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < user.getTags().size(); i++){
                tags.add(user.getTags().get(i).getTitle());
            }
            viewHolder.flowLayout.setFlowLayout(tags, null);
        }
        else {
            List<String> tags = new ArrayList<>();
            viewHolder.flowLayout.setFlowLayout(tags, null);
        }

        viewHolder.iconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bridge.getInstance().showUser(context, user);
            }
        });

        viewHolder.nameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bridge.getInstance().showUser(context, user);
            }
        });

        viewHolder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "联系他", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    static class UserViewHolder{

        ImageView iconImage;
        CImageView callBtn;
        TextView nameText;
        ImageView starsImage;
        TextView companyText;
        TextView chargeText;
        FlowLayout flowLayout;

    }

}
