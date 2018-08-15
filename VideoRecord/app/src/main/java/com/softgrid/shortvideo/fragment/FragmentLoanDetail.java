package com.softgrid.shortvideo.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.customView.CImageView;
import com.softgrid.shortvideo.info.Bridge;
import com.softgrid.shortvideo.model.Loans;
import com.softgrid.shortvideo.tool.DeviceInfoTool;
import com.softgrid.shortvideo.utils.AppHelper;

/**
 * Created by tianfeng on 2018/7/16.
 */

public class FragmentLoanDetail extends BaseFragment {

    private View rootView;

    private View mFileBtn;
    private View mUpdateBtn;

    private ImageView mBuildingImage;
    private TextView mBuildingTitle;
    private TextView mBuildingDesc;
    private TextView mLoanTime;
    private TextView mLoanMoney;
    private TextView mLoanYear;

    private View mContactConsult;
    private View mContactLayout;
    private ImageView mContactIcon;
    private TextView mContactName;
    private ImageView mContactStars;
    private TextView mContactCompany;
    private CImageView mContactCallBtn;

    private View mStatusView;
    private View mStatusText;

    private View mStatusUpdateView;
    private View mStatusUpdateText;

    private Loans loans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.frag_loandetail, container, false);
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
        else if (v == mFileBtn){

        }
        else if (v == mUpdateBtn){

        }
        else if (v == mBuildingImage || v == mBuildingTitle || v == mBuildingDesc){

        }
        else if (v == mContactConsult){

        }
        else if (v == mContactLayout || v == mContactName || v == mContactIcon){

        }
        else if (v == mContactCallBtn){

        }

    }

    public void initUI(View view) {
        initHeadView(view, this);

        rootView = view.findViewById(R.id.root_view);

        mFileBtn = view.findViewById(R.id.file_layout);
        mUpdateBtn = view.findViewById(R.id.update_layout);

        mBuildingImage = view.findViewById(R.id.building_image);
        mBuildingTitle = view.findViewById(R.id.building_title);
        mBuildingDesc = view.findViewById(R.id.building_desc);
        mLoanTime = view.findViewById(R.id.loan_time_text);
        mLoanMoney = view.findViewById(R.id.loan_money_text);
        mLoanYear = view.findViewById(R.id.loan_year_text);

        mContactConsult = view.findViewById(R.id.consult_text);
        mContactLayout = view.findViewById(R.id.contacts_layout);
        mContactIcon = view.findViewById(R.id.contacts_icon);
        mContactName = view.findViewById(R.id.contacts_name);
        mContactStars = view.findViewById(R.id.contacts_stars);
        mContactCompany = view.findViewById(R.id.contacts_company);
        mContactCallBtn = view.findViewById(R.id.contacts_callbtn);

        mStatusView = view.findViewById(R.id.loan_status_layout);
        mStatusText = view.findViewById(R.id.loan_status_text);

        mStatusUpdateView = view.findViewById(R.id.loan_status_update_layout);
        mStatusUpdateText = view.findViewById(R.id.loan_status_update_text);

        int width = DeviceInfoTool.getScreenWidth(getActivity());
        int height = width*705/1125 + 1;
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(width, height);
        mBuildingImage.setLayoutParams(params);
    }

    private void addActions(){
        rootView.setOnClickListener(this);

        leftImage.setImageResource(R.drawable.fc_nav_back);
        rightImage.setImageResource(R.drawable.fc_nav_back);
        rightImage.setVisibility(View.INVISIBLE);

        mFileBtn.setOnClickListener(this);
        mUpdateBtn.setOnClickListener(this);

        mBuildingImage.setOnClickListener(this);
        mBuildingTitle.setOnClickListener(this);
        mBuildingDesc.setOnClickListener(this);

        mContactConsult.setOnClickListener(this);
        mContactLayout.setOnClickListener(this);
        mContactCallBtn.setOnClickListener(this);
        mContactName.setOnClickListener(this);
        mContactIcon.setOnClickListener(this);
    }

    public void initData(){
        titleText.setText(R.string.loan_detail);

        if (Bridge.getInstance().loans != null){
            loans = Bridge.getInstance().loans;
            Bridge.getInstance().loans = null;
        }
        updateUI();
    }

    private void updateUI(){

        mLoanTime.setText(AppHelper.getInstance().stampToDateString(
                loans.getRequestTime(), "yyyy-MM-dd"));
        mLoanMoney.setText(AppHelper.getInstance().formatPrice(getActivity(),
                loans.getTotal(), getString(R.string.price_total_2)));
        mLoanYear.setText(loans.getYear() + getString(R.string.year));

        mBuildingTitle.setText(loans.getBuilding().getTitle());
        Glide.with(getActivity().getApplicationContext())
                .load(Uri.parse(loans.getBuilding().getOriginal()))
                .apply(AppHelper.getInstance().getBannerOptions())
                .into(mBuildingImage);
        String descText;
        descText = loans.getBuilding().getRooms() + getString(R.string.room_unit);
        descText = descText +  " | "  + loans.getBuilding().getArea() + getString(R.string.area_unit);
        if (loans.getBuilding().getDecorate() > 0){
            descText = descText + " | " +
                    AppHelper.getInstance().getDecorate(getActivity(), loans.getBuilding().getDecorate());
        }
        if (loans.getBuilding().getOrientation() > 0){
            descText = descText + " | " +
                    AppHelper.getInstance().getOrientation(getActivity(), loans.getBuilding().getOrientation());
        }
        mBuildingDesc.setText(descText);

        if (loans.getBanker() != null){  //选择了中介
            mContactLayout.setVisibility(View.VISIBLE);
            mContactName.setText(loans.getBanker().getName());
            mContactCompany.setText(loans.getBanker().getCompany());
            if (loans.getBanker().getImage() != null){
                Glide.with(getActivity().getApplicationContext())
                        .load(Uri.parse(loans.getBanker().getImage()))
                        .apply(AppHelper.getInstance().getUserOptions())
                        .into(mContactIcon);
            }

            if (loans.getBanker().getRate() < 20){
                mContactStars.setImageResource(R.drawable.fc_stars_01);
            }
            else if (loans.getBanker().getRate() < 30){
                mContactStars.setImageResource(R.drawable.fc_stars_02);
            }
            else if (loans.getBanker().getRate() < 40){
                mContactStars.setImageResource(R.drawable.fc_stars_03);
            }
            else if (loans.getBanker().getRate() < 50){
                mContactStars.setImageResource(R.drawable.fc_stars_04);
            }
            else {
                mContactStars.setImageResource(R.drawable.fc_stars_05);
            }

        }

    }
}
