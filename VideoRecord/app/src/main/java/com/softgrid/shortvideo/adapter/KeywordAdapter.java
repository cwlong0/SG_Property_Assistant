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

public class KeywordAdapter extends BaseAdapter {

    private Activity context;
    private String keyword;

    private ArrayList<String> keyList;

    public KeywordAdapter(Activity activity){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_keyword, null);

            viewHolder.keywordCover = convertView.findViewById(R.id.keyword_icon);
            viewHolder.keywordTitle = convertView.findViewById(R.id.keyword_title);
            viewHolder.keywordCount = convertView.findViewById(R.id.keyword_count);
            viewHolder.keywordDesc = convertView.findViewById(R.id.keyword_type);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String text = getItem(position);
        viewHolder.keywordTitle.setText(text);
        viewHolder.keywordDesc.setText("");
        viewHolder.keywordCount.setVisibility(View.GONE);
        viewHolder.keywordCover.setImageResource(R.drawable.coll_sruew_ban);

        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        ForegroundColorSpan unSelectSpan = new ForegroundColorSpan(
                context.getResources().getColor(R.color.color_detail));

        builder.setSpan(unSelectSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        int begin = 0;
        int length = keyword.length();
        int start = text.indexOf(keyword, begin);
        int end = 0;
        if (start > -1){
            end = start + length;
        }

        while (start > -1){
            ForegroundColorSpan selectSpan = new ForegroundColorSpan(
                    context.getResources().getColor(R.color.color_text));
            builder.setSpan(selectSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            begin = end;
            start = text.indexOf(keyword, begin);
            if (start > -1){
                end = start + length;
            }
        }

        viewHolder.keywordTitle.setText(builder);

        return convertView;
    }

    static class ViewHolder{

        ImageView keywordCover;
        TextView keywordTitle;
        TextView keywordCount;
        TextView keywordDesc;

    }

}
