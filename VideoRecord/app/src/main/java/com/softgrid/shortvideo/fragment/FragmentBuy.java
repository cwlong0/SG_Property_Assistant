package com.softgrid.shortvideo.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.TabActivity;
import com.softgrid.shortvideo.customView.CImageView;
import com.softgrid.shortvideo.info.Bridge;
import com.softgrid.shortvideo.model.Building;
import com.softgrid.shortvideo.tool.DeviceInfoTool;
import com.softgrid.shortvideo.utils.AppHelper;

/**
 * Created by tianfeng on 2018/7/17.
 */

public class FragmentBuy extends BaseFragment {

    private View rootView;

    private View mBuyBtn;

    private ImageView mBuyBuildingImage;
    private TextView mBuyBuildingTitle;
    private TextView mBuyBuildingDesc;

    private View mContactLayout;
    private ImageView mContactIcon;
    private TextView mContactName;
    private View mContactTypeLayout;
    private TextView mContactTypeText;
    private ImageView mContactStars;
    private TextView mContactCompany;
    private CImageView mContactCallBtn;

    private TextView mBuyMoneyText;

    private View mBuyLoanLayout;
    private ImageView mBuyLoanSwitch;

    private View mBuyLoanDescLayout;
    private View mBuyLoanMoneyLayout;
    private TextView mBuyLoanMoneyText;
    private View mBuyLoanYearLayout;
    private TextView mBuyLoanYearText;
    private View mBuyLoanConsultLayout;

    private View mBuyTimeLayout;
    private TextView mBuyTimeText;

    private Building building;

    private float buyMoney = 3000000;
    private long buyTime;
    private boolean haveLoan = true;
    private float loanMoney = 500000;
    private int loanYear = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.frag_buy, container, false);
        initUI(view);
        addActions();
        initData();
        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        updateUI();
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            updateUI();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        if (v == leftImage){
            Bridge.getInstance().goBack();
        }
        else if(v == mBuyBtn){

        }
        else if(v == mBuyBuildingImage || v == mBuyBuildingTitle || v == mBuyBuildingDesc){
            Bridge.getInstance().showBuilding(getActivity(), building);
        }
        else if(v == mContactIcon || v == mContactName){
            Bridge.getInstance().showUser(getActivity(), building.getIntermediary());
        }
        else if(v == mContactLayout){
            Bridge.getInstance().gotoFragment(TabActivity.FRAG_IntermediaryList);
        }
        else if(v == mContactCallBtn){

        }
        else if(v == mBuyLoanSwitch){
            haveLoan = !haveLoan;
            updateUI();
        }
        else if(v == mBuyLoanMoneyLayout){
            Toast.makeText(getActivity(), "贷款金额设置", Toast.LENGTH_SHORT).show();
        }
        else if(v == mBuyLoanYearLayout){
            Toast.makeText(getActivity(), "贷款年限设置", Toast.LENGTH_SHORT).show();
        }
        else if(v == mBuyLoanConsultLayout){
            Toast.makeText(getActivity(), "贷款咨询", Toast.LENGTH_SHORT).show();
        }
        else if(v == mBuyTimeLayout){
            Toast.makeText(getActivity(), "成交时间设置", Toast.LENGTH_SHORT).show();
        }

    }

    public void initUI(View view) {
        initHeadView(view, this);
        leftImage.setImageResource(R.drawable.fc_nav_back);
        rightImage.setImageResource(R.drawable.fc_nav_back);
        rightImage.setVisibility(View.INVISIBLE);

        rootView = view.findViewById(R.id.root_view);

        mBuyBtn = view.findViewById(R.id.buy_layout);

        mBuyBuildingImage = view.findViewById(R.id.building_image);
        mBuyBuildingTitle = view.findViewById(R.id.building_title);
        mBuyBuildingDesc = view.findViewById(R.id.building_desc);

        mContactLayout = view.findViewById(R.id.building_contacts_layout);
        mContactIcon = view.findViewById(R.id.building_contacts_icon);
        mContactName = view.findViewById(R.id.building_contacts_name);
        mContactTypeLayout = view.findViewById(R.id.building_contacts_type_layout);
        mContactTypeText = view.findViewById(R.id.building_contacts_type_title);
        mContactStars = view.findViewById(R.id.building_contacts_stars);
        mContactCompany = view.findViewById(R.id.building_contacts_company);
        mContactCallBtn = view.findViewById(R.id.building_contacts_callbtn);

        mBuyMoneyText = view.findViewById(R.id.buy_price_text);

        mBuyLoanLayout = view.findViewById(R.id.buy_loan_title_layout);
        mBuyLoanSwitch = view.findViewById(R.id.buy_loan_switch);

        mBuyLoanDescLayout = view.findViewById(R.id.buy_loan_desc_layout);
        mBuyLoanMoneyLayout = view.findViewById(R.id.buy_loan_money_layout);
        mBuyLoanMoneyText = view.findViewById(R.id.buy_loan_money_text);
        mBuyLoanYearLayout = view.findViewById(R.id.buy_loan_year_layout);
        mBuyLoanYearText = view.findViewById(R.id.buy_loan_year_text);
        mBuyLoanConsultLayout = view.findViewById(R.id.buy_loan_consult_layout);

        mBuyTimeLayout = view.findViewById(R.id.buy_time_layout);
        mBuyTimeText = view.findViewById(R.id.buy_time_text);

        int width = DeviceInfoTool.getScreenWidth(getActivity());
        int height = width*705/1125 + 1;
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(width, height);
        mBuyBuildingImage.setLayoutParams(params);
    }

    private void addActions(){
        rootView.setOnClickListener(this);
        mBuyBtn.setOnClickListener(this);

        mBuyBuildingImage.setOnClickListener(this);
        mBuyBuildingTitle.setOnClickListener(this);
        mBuyBuildingDesc.setOnClickListener(this);

        mContactLayout.setOnClickListener(this);
        mContactCallBtn.setOnClickListener(this);
        mContactName.setOnClickListener(this);
        mContactIcon.setOnClickListener(this);

        mBuyLoanSwitch.setOnClickListener(this);

        mBuyLoanMoneyLayout.setOnClickListener(this);
        mBuyLoanYearLayout.setOnClickListener(this);
        mBuyLoanConsultLayout.setOnClickListener(this);

        mBuyTimeLayout.setOnClickListener(this);

    }

    public void initData(){
        titleText.setText(R.string.take_up);

        if (Bridge.getInstance().building != null){
            building = Bridge.getInstance().building;
            Bridge.getInstance().building = null;
        }

        buyTime = System.currentTimeMillis();
        buyMoney = building.getTotalPrice();

        updateUI();
    }

    private void updateUI(){

        mBuyMoneyText.setText(AppHelper.getInstance().formatPrice(getActivity(),
                buyMoney, getString(R.string.price_total_2)));
        mBuyLoanMoneyText.setText(AppHelper.getInstance().formatPrice(getActivity(),
                loanMoney, getString(R.string.price_total_2)));
        mBuyLoanYearText.setText(loanYear + getString(R.string.year));
        mBuyTimeText.setText(AppHelper.getInstance().stampToDateString(
                buyTime, "yyyy-MM-dd HH:mm"));

        mBuyBuildingTitle.setText(building.getTitle());
        Glide.with(getActivity().getApplicationContext())
                .load(Uri.parse(building.getOriginal()))
                .apply(AppHelper.getInstance().getBannerOptions())
                .into(mBuyBuildingImage);
        String descText;
        descText = building.getRooms() + getString(R.string.room_unit);
        descText = descText +  " | "  + building.getArea() + getString(R.string.area_unit);
        if (building.getDecorate() > 0){
            descText = descText + " | " +
                    AppHelper.getInstance().getDecorate(getActivity(), building.getDecorate());
        }
        if (building.getOrientation() > 0){
            descText = descText + " | " +
                    AppHelper.getInstance().getOrientation(getActivity(), building.getOrientation());
        }
        mBuyBuildingDesc.setText(descText);

        if (building.getIntermediary() != null){  //选择了中介
            mContactLayout.setVisibility(View.VISIBLE);
            mContactName.setText(building.getIntermediary().getName());
            mContactCompany.setText(building.getIntermediary().getCompany());
            if (building.getIntermediary().getImage() != null){
                Glide.with(getActivity().getApplicationContext())
                        .load(Uri.parse(building.getIntermediary().getImage()))
                        .apply(AppHelper.getInstance().getUserOptions())
                        .into(mContactIcon);
            }

            if (building.getIntermediary().getRate() < 20){
                mContactStars.setImageResource(R.drawable.fc_stars_01);
            }
            else if (building.getIntermediary().getRate() < 30){
                mContactStars.setImageResource(R.drawable.fc_stars_02);
            }
            else if (building.getIntermediary().getRate() < 40){
                mContactStars.setImageResource(R.drawable.fc_stars_03);
            }
            else if (building.getIntermediary().getRate() < 50){
                mContactStars.setImageResource(R.drawable.fc_stars_04);
            }
            else {
                mContactStars.setImageResource(R.drawable.fc_stars_05);
            }

        }

        if (haveLoan){
            mBuyLoanSwitch.setImageResource(R.drawable.fc_switch_open);
            mBuyLoanDescLayout.setVisibility(View.VISIBLE);
        }
        else {
            mBuyLoanSwitch.setImageResource(R.drawable.fc_switch_no);
            mBuyLoanDescLayout.setVisibility(View.GONE);
        }

    }
}
