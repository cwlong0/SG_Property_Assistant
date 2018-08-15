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
import com.softgrid.shortvideo.model.Building;
import com.softgrid.shortvideo.model.Loans;
import com.softgrid.shortvideo.utils.AppHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianfeng on 2017/8/25.
 */

public class LoanAdapter extends BaseAdapter {

    private Context context;
    private List<Loans> itemList;

    public LoanAdapter(Context context){
        this.context = context;
        itemList = new ArrayList<Loans>();
    }

    public void setItemsData(ArrayList<Loans> dataList){
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
    public Loans getItem(int position) {

        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LoanViewHolder viewHolder;
        if (convertView != null && convertView.getTag() instanceof LoanViewHolder){

            viewHolder = (LoanViewHolder)convertView.getTag();
        }
        else{
            viewHolder = new LoanViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.cell_loan, null);
            viewHolder.iconImage = convertView.findViewById(R.id.building_image);
            viewHolder.typeImage = convertView.findViewById(R.id.building_type_bg);
            viewHolder.typeText = convertView.findViewById(R.id.building_type_text);
            viewHolder.titleText = convertView.findViewById(R.id.building_title);
            viewHolder.descText = convertView.findViewById(R.id.building_desc);
            viewHolder.totalPriceText = convertView.findViewById(R.id.building_total_text);
            viewHolder.unitPriceText = convertView.findViewById(R.id.building_unit_text);
            viewHolder.loanMoneyText = convertView.findViewById(R.id.loan_money);
            viewHolder.loanYearText = convertView.findViewById(R.id.loan_year);
            viewHolder.loanStatusView = convertView.findViewById(R.id.loan_status_layout);
            viewHolder.loanStatusText = convertView.findViewById(R.id.loan_status_text);
            viewHolder.loanTimeText = convertView.findViewById(R.id.loan_time_text);

            convertView.setTag(viewHolder);
        }

        Loans loans = getItem(position);

        if (loans.getBuilding().getThumbnail() != null){
            Glide.with(context.getApplicationContext())
                    .load(Uri.parse(loans.getBuilding().getThumbnail()))
                    .apply(AppHelper.getInstance().getImageOptions())
                    .into(viewHolder.iconImage);
        }

        viewHolder.titleText.setText(loans.getBuilding().getTitle());
        String descText = "";
        descText = loans.getBuilding().getRooms() + context.getString(R.string.room_unit);
        descText = descText +  " | "  + loans.getBuilding().getArea() + context.getString(R.string.area_unit);
        if (loans.getBuilding().getDecorate() > 0){
            descText = descText + " | " +
                    AppHelper.getInstance().getDecorate(context, loans.getBuilding().getDecorate());
        }
        if (loans.getBuilding().getOrientation() > 0){
            descText = descText + " | " +
                    AppHelper.getInstance().getOrientation(context, loans.getBuilding().getOrientation());
        }
        viewHolder.descText.setText(descText);

        if (loans.getBuilding().getIsResale() > 0){  //二手房
            viewHolder.typeImage.setVisibility(View.GONE);
            viewHolder.typeText.setVisibility(View.GONE);

            viewHolder.unitPriceText.setVisibility(View.VISIBLE);
            viewHolder.totalPriceText.setText(AppHelper.getInstance().formatPrice(context,
                    loans.getBuilding().getTotalPrice(), ""));
            viewHolder.unitPriceText.setText(AppHelper.getInstance().formatPrice(context,
                    loans.getBuilding().getUnitPrice(), context.getString(R.string.price_unit_1)));

        }
        else {  //新房
            viewHolder.totalPriceText.setText(AppHelper.getInstance().formatPrice(context,
                    loans.getBuilding().getUnitPrice(), context.getString(R.string.price_unit_1)));
            viewHolder.unitPriceText.setVisibility(View.GONE);
            viewHolder.typeImage.setVisibility(View.VISIBLE);
            viewHolder.typeText.setVisibility(View.VISIBLE);

        }

        viewHolder.loanMoneyText.setText(AppHelper.getInstance().formatPrice(context,
                loans.getTotal(), ""));
        viewHolder.loanYearText.setText(loans.getYear() + context.getString(R.string.year));

        viewHolder.loanTimeText.setText(AppHelper.getInstance().stampToDateString(
                loans.getRequestTime(), "yyyy-MM-dd"));

        return convertView;
    }

    static class LoanViewHolder{

        ImageView iconImage;
        ImageView typeImage;
        TextView typeText;
        TextView titleText;
        TextView descText;
        TextView totalPriceText;
        TextView unitPriceText;
        TextView loanMoneyText;
        TextView loanYearText;
        View loanStatusView;
        TextView loanStatusText;
        TextView loanTimeText;
    }

}
