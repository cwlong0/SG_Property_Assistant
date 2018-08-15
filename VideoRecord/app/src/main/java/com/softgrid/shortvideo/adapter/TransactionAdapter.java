package com.softgrid.shortvideo.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.model.Loans;
import com.softgrid.shortvideo.model.Transaction;
import com.softgrid.shortvideo.utils.AppHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianfeng on 2017/8/25.
 */

public class TransactionAdapter extends BaseAdapter {

    private Context context;
    private List<Transaction> itemList;

    public TransactionAdapter(Context context){
        this.context = context;
        itemList = new ArrayList<Transaction>();
    }

    public void setItemsData(ArrayList<Transaction> dataList){
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
    public Transaction getItem(int position) {

        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        TransactionViewHolder viewHolder;
        if (convertView != null && convertView.getTag() instanceof TransactionViewHolder){

            viewHolder = (TransactionViewHolder)convertView.getTag();
        }
        else{
            viewHolder = new TransactionViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.cell_transaction, null);
            viewHolder.iconImage = convertView.findViewById(R.id.building_image);
            viewHolder.typeImage = convertView.findViewById(R.id.building_type_bg);
            viewHolder.typeText = convertView.findViewById(R.id.building_type_text);
            viewHolder.titleText = convertView.findViewById(R.id.building_title);
            viewHolder.descText = convertView.findViewById(R.id.building_desc);
            viewHolder.totalPriceText = convertView.findViewById(R.id.building_total_text);
            viewHolder.unitPriceText = convertView.findViewById(R.id.building_unit_text);

            viewHolder.statusView = convertView.findViewById(R.id.tran_status_layout);
            viewHolder.statusText = convertView.findViewById(R.id.tran_status_text);

            viewHolder.loanStatusView = convertView.findViewById(R.id.loan_status_layout);
            viewHolder.loanStatusText = convertView.findViewById(R.id.loan_status_text);
            viewHolder.timeText = convertView.findViewById(R.id.loan_time_text);

            convertView.setTag(viewHolder);
        }

        Transaction transaction = getItem(position);

        if (transaction.getBuilding().getThumbnail() != null){
            Glide.with(context.getApplicationContext())
                    .load(Uri.parse(transaction.getBuilding().getThumbnail()))
                    .apply(AppHelper.getInstance().getImageOptions())
                    .into(viewHolder.iconImage);
        }

        viewHolder.titleText.setText(transaction.getBuilding().getTitle());
        String descText = "";
        descText = transaction.getBuilding().getRooms() + context.getString(R.string.room_unit);
        descText = descText +  " | "  + transaction.getBuilding().getArea() + context.getString(R.string.area_unit);
        if (transaction.getBuilding().getDecorate() > 0){
            descText = descText + " | " +
                    AppHelper.getInstance().getDecorate(context, transaction.getBuilding().getDecorate());
        }
        if (transaction.getBuilding().getOrientation() > 0){
            descText = descText + " | " +
                    AppHelper.getInstance().getOrientation(context, transaction.getBuilding().getOrientation());
        }
        viewHolder.descText.setText(descText);

        if (transaction.getBuilding().getIsResale() > 0){  //二手房
            viewHolder.typeImage.setVisibility(View.GONE);
            viewHolder.typeText.setVisibility(View.GONE);

            viewHolder.unitPriceText.setVisibility(View.VISIBLE);
            viewHolder.totalPriceText.setText(AppHelper.getInstance().formatPrice(context,
                    transaction.getBuilding().getTotalPrice(), ""));
            viewHolder.unitPriceText.setText(AppHelper.getInstance().formatPrice(context,
                    transaction.getBuilding().getUnitPrice(), context.getString(R.string.price_unit_1)));

        }
        else {  //新房
            viewHolder.totalPriceText.setText(AppHelper.getInstance().formatPrice(context,
                    transaction.getBuilding().getUnitPrice(), context.getString(R.string.price_unit_1)));
            viewHolder.unitPriceText.setVisibility(View.GONE);
            viewHolder.typeImage.setVisibility(View.VISIBLE);
            viewHolder.typeText.setVisibility(View.VISIBLE);

        }

        viewHolder.timeText.setText(AppHelper.getInstance().stampToDateString(
                transaction.getBuyTime(), "yyyy-MM-dd"));

        return convertView;
    }

    static class TransactionViewHolder{

        ImageView iconImage;
        ImageView typeImage;
        TextView typeText;
        TextView titleText;
        TextView descText;
        TextView totalPriceText;
        TextView unitPriceText;

        View statusView;
        TextView statusText;

        View loanStatusView;
        TextView loanStatusText;

        TextView timeText;
    }

}
