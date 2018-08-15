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
import com.softgrid.shortvideo.model.Transaction;
import com.softgrid.shortvideo.tool.DeviceInfoTool;
import com.softgrid.shortvideo.utils.AppHelper;

/**
 * Created by tianfeng on 2018/7/16.
 */

public class FragmentTransactionDetail extends BaseFragment {

    private View rootView;

    private View mFileBtn;
    private View mUpdateBtn;

    private ImageView mBuildingImage;
    private TextView mBuildingTitle;
    private TextView mBuildingDesc;

    private View mContactAgencyLayout;
    private ImageView mContactAgencyIcon;
    private TextView mContactAgencyName;
    private ImageView mContactAgencyStars;
    private TextView mContactAgencyCompany;
    private CImageView mContactAgencyCallBtn;

    private View mContactLawyerLayout;
    private ImageView mContactLawyerIcon;
    private TextView mContactLawyerName;
    private ImageView mContactLawyerStars;
    private TextView mContactLawyerCompany;
    private CImageView mContactLawyerCallBtn;

    private View mStatusAgencyView;
    private View mStatusAgencyText;
    private View mStatusUpdateAgencyView;
    private View mStatusUpdateAgencyText;

    private View mStatusLawyerView;
    private View mStatusLawyerText;
    private View mStatusUpdateLawyerView;
    private View mStatusUpdateLawyerText;

    private Transaction transaction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.frag_transactiondetail, container, false);
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
        else if (v == mContactAgencyLayout){

        }
        else if (v == mContactAgencyCallBtn){

        }
        else if (v == mContactLawyerLayout){

        }
        else if (v == mContactLawyerCallBtn){

        }
        else if (v == mStatusUpdateAgencyView){

        }
        else if (v == mStatusUpdateLawyerView){

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

        mContactAgencyLayout = view.findViewById(R.id.contacts_layout);
        mContactAgencyIcon = view.findViewById(R.id.contacts_icon);
        mContactAgencyName = view.findViewById(R.id.contacts_name);
        mContactAgencyStars = view.findViewById(R.id.contacts_stars);
        mContactAgencyCompany = view.findViewById(R.id.contacts_company);
        mContactAgencyCallBtn = view.findViewById(R.id.contacts_callbtn);

        mContactLawyerLayout = view.findViewById(R.id.contacts_layout2);
        mContactLawyerIcon = view.findViewById(R.id.contacts_icon2);
        mContactLawyerName = view.findViewById(R.id.contacts_name2);
        mContactLawyerStars = view.findViewById(R.id.contacts_stars2);
        mContactLawyerCompany = view.findViewById(R.id.contacts_company2);
        mContactLawyerCallBtn = view.findViewById(R.id.contacts_callbtn2);

        mStatusAgencyView = view.findViewById(R.id.loan_status_layout);
        mStatusAgencyText = view.findViewById(R.id.loan_status_text);
        mStatusUpdateAgencyView = view.findViewById(R.id.loan_status_update_layout);
        mStatusUpdateAgencyText = view.findViewById(R.id.loan_status_update_text);

        mStatusLawyerView = view.findViewById(R.id.transaction_status_layout);
        mStatusLawyerText = view.findViewById(R.id.transaction_status_text);
        mStatusUpdateLawyerView = view.findViewById(R.id.transaction_status_update_layout);
        mStatusUpdateLawyerText = view.findViewById(R.id.transaction_status_update_text);

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

        mContactAgencyLayout.setOnClickListener(this);
        mContactAgencyCallBtn.setOnClickListener(this);
        mContactAgencyName.setOnClickListener(this);
        mContactAgencyIcon.setOnClickListener(this);

        mStatusUpdateAgencyView.setOnClickListener(this);

        mContactLawyerLayout.setOnClickListener(this);
        mContactLawyerCallBtn.setOnClickListener(this);
        mContactLawyerName.setOnClickListener(this);
        mContactLawyerIcon.setOnClickListener(this);

        mStatusUpdateLawyerView.setOnClickListener(this);
    }

    public void initData(){
        titleText.setText(R.string.transaction_detail);
        if (Bridge.getInstance().transaction != null){
            transaction = Bridge.getInstance().transaction;
            Bridge.getInstance().transaction = null;
        }
        updateUI();
    }

    private void updateUI(){

        mBuildingTitle.setText(transaction.getBuilding().getTitle());
        Glide.with(getActivity().getApplicationContext())
                .load(Uri.parse(transaction.getBuilding().getOriginal()))
                .apply(AppHelper.getInstance().getBannerOptions())
                .into(mBuildingImage);
        String descText;
        descText = transaction.getBuilding().getRooms() + getString(R.string.room_unit);
        descText = descText +  " | "  + transaction.getBuilding().getArea() + getString(R.string.area_unit);
        if (transaction.getBuilding().getDecorate() > 0){
            descText = descText + " | " +
                    AppHelper.getInstance().getDecorate(getActivity(), transaction.getBuilding().getDecorate());
        }
        if (transaction.getBuilding().getOrientation() > 0){
            descText = descText + " | " +
                    AppHelper.getInstance().getOrientation(getActivity(), transaction.getBuilding().getOrientation());
        }
        mBuildingDesc.setText(descText);

        if (transaction.getIntermediary() != null){  //选择了中介
            mContactAgencyLayout.setVisibility(View.VISIBLE);
            mContactAgencyName.setText(transaction.getIntermediary().getName());
            mContactAgencyCompany.setText(transaction.getIntermediary().getCompany());
            if (transaction.getIntermediary().getImage() != null){
                Glide.with(getActivity().getApplicationContext())
                        .load(Uri.parse(transaction.getIntermediary().getImage()))
                        .apply(AppHelper.getInstance().getUserOptions())
                        .into(mContactAgencyIcon);
            }

            if (transaction.getIntermediary().getRate() < 20){
                mContactAgencyStars.setImageResource(R.drawable.fc_stars_01);
            }
            else if (transaction.getIntermediary().getRate() < 30){
                mContactAgencyStars.setImageResource(R.drawable.fc_stars_02);
            }
            else if (transaction.getIntermediary().getRate() < 40){
                mContactAgencyStars.setImageResource(R.drawable.fc_stars_03);
            }
            else if (transaction.getIntermediary().getRate() < 50){
                mContactAgencyStars.setImageResource(R.drawable.fc_stars_04);
            }
            else {
                mContactAgencyStars.setImageResource(R.drawable.fc_stars_05);
            }

        }

        if (transaction.getLawyer() != null){  //选择了中介
            mContactLawyerLayout.setVisibility(View.VISIBLE);
            mContactLawyerName.setText(transaction.getLawyer().getName());
            mContactLawyerCompany.setText(transaction.getLawyer().getCompany());
            if (transaction.getLawyer().getImage() != null){
                Glide.with(getActivity().getApplicationContext())
                        .load(Uri.parse(transaction.getLawyer().getImage()))
                        .apply(AppHelper.getInstance().getUserOptions())
                        .into(mContactLawyerIcon);
            }

            if (transaction.getLawyer().getRate() < 20){
                mContactLawyerStars.setImageResource(R.drawable.fc_stars_01);
            }
            else if (transaction.getLawyer().getRate() < 30){
                mContactLawyerStars.setImageResource(R.drawable.fc_stars_02);
            }
            else if (transaction.getLawyer().getRate() < 40){
                mContactLawyerStars.setImageResource(R.drawable.fc_stars_03);
            }
            else if (transaction.getLawyer().getRate() < 50){
                mContactLawyerStars.setImageResource(R.drawable.fc_stars_04);
            }
            else {
                mContactLawyerStars.setImageResource(R.drawable.fc_stars_05);
            }

        }

    }
}
