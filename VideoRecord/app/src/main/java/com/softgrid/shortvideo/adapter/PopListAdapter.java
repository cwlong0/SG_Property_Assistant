package com.softgrid.shortvideo.adapter;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.softgrid.shortvideo.R;

import java.util.ArrayList;


/**
 * Created by tianfeng on 2017/8/31.
 */

public class PopListAdapter extends BaseAdapter {

    private Activity context;
    private String keyword;

    private ArrayList<String> keyList;

    public PopListAdapter(Activity activity){
        this.context = activity;
        keyList = new ArrayList<>();
        keyword = "";
    }

    public void setData(ArrayList<String> dataList){
        keyList.clear();
        if (dataList != null){
            keyList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public void setKeyword(String keyword){
        this.keyword = keyword;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return keyList.size();
    }

    @Override
    public String getItem(int position) {

        return keyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_poplist, null);

            viewHolder.keywordTitle = convertView.findViewById(R.id.keyword_title);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String text = getItem(position);
        viewHolder.keywordTitle.setText(text);

        if (keyword != null && keyword.equals(text)){
            viewHolder.keywordTitle.setTextColor(context.getColor(R.color.main_top));
        }
        else {
            viewHolder.keywordTitle.setTextColor(context.getColor(R.color.color_text));
        }

        return convertView;
    }

    static class ViewHolder{

        TextView keywordTitle;

    }

}
