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
import com.softgrid.shortvideo.model.Msg;
import com.softgrid.shortvideo.utils.AppHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianfeng on 2017/8/25.
 */

public class MsgAdapter extends BaseAdapter {


    private Context context;
    private List<Msg> itemList;


    public MsgAdapter(Context context){
        this.context = context;
        itemList = new ArrayList<Msg>();
    }

    public void setItemsData(ArrayList<Msg> dataList){
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
    public Msg getItem(int position) {

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
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_msg, null);
            viewHolder.iconImage = convertView.findViewById(R.id.msg_icon);
            viewHolder.titleText = convertView.findViewById(R.id.msg_title);
            viewHolder.descText = convertView.findViewById(R.id.msg_desc);
            viewHolder.statusLayout = convertView.findViewById(R.id.msg_status_layout);
            viewHolder.statusText = convertView.findViewById(R.id.msg_status_title);

            convertView.setTag(viewHolder);
        }

        return convertView;
    }

    static class UserViewHolder{

        ImageView iconImage;
        TextView titleText;
        TextView descText;
        View statusLayout;
        TextView statusText;

    }

}
