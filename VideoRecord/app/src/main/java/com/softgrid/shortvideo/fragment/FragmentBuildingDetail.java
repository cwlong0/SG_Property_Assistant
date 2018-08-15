package com.softgrid.shortvideo.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.TabActivity;
import com.softgrid.shortvideo.adapter.ImageAdapter;
import com.softgrid.shortvideo.customView.CImageView;
import com.softgrid.shortvideo.customView.FlowLayout;
import com.softgrid.shortvideo.utils.AppHelper;
import com.softgrid.shortvideo.info.Bridge;
import com.softgrid.shortvideo.model.Building;
import com.softgrid.shortvideo.tool.DeviceInfoTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianfeng on 2018/7/12.
 */

public class FragmentBuildingDetail extends BaseFragment {

    private View rootView;

    private View mCallBtn;                     //咨询按钮
    private View mCallText;                    //咨询text
    private View mTakeUpBtn;                   //申请认购
    private View mSubscribeBtn;                //预约看房

    private RelativeLayout discoveryLayout;

    private ViewPager mViewPager;
    private TextView mNumberText;
    private View mTypeImage;
    private View mTypeText;
    private TextView mTitleText;
    private TextView mPriceText;
    private TextView mAreaText;
    private TextView mRoomText;
    private CImageView mFavBtn;
    private TextView mFavText;
    private TextView mLoanConsultText;

    private TextView mOneOneTitleText;
    private TextView mOneTwoTitleText;
    private TextView mTwoOneTitleText;
    private TextView mTwoTwoTitleText;
    private TextView mThreeOneTitleText;
    private TextView mThreeTwoTitleText;
    private TextView mFourOneTitleText;
    private TextView mFourTwoTitleText;
    private TextView mFiveTitleText;

    private TextView mOneOneDescText;
    private TextView mOneTwoDescText;
    private TextView mTwoOneDescText;
    private TextView mTwoTwoDescText;
    private TextView mThreeOneDescText;
    private TextView mThreeTwoDescText;
    private TextView mFourOneDescText;
    private TextView mFourTwoDescText;
    private TextView mFiveDescText;

    private CImageView mLocationBtn;

    private FlowLayout mFlowLayout;

    private View mContactLayout;
    private ImageView mContactIcon;
    private TextView mContactName;
    private View mContactTypeLayout;
    private TextView mContactTypeText;
    private ImageView mContactStars;
    private TextView mContactCompany;
    private CImageView mContactCallBtn;

    private TextView mDescText;

    private Building building;

    private ImageAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.frag_building, container, false);
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
        else if(v == mCallBtn || v == mCallText){
            if (building.getIntermediary() != null){
                Toast.makeText(getActivity(), "咨询中介", Toast.LENGTH_SHORT).show();
            }
            else {
                Bridge.getInstance().gotoFragment(TabActivity.FRAG_IntermediaryList);
            }
        }
        else if(v == mSubscribeBtn){
//            Toast.makeText(getActivity(), "预约看房", Toast.LENGTH_SHORT).show();
            Bridge.getInstance().building = building;
            Bridge.getInstance().gotoFragment(TabActivity.FRAG_Bespoke);
        }
        else if(v == mTakeUpBtn){
//            Toast.makeText(getActivity(), "申请认购", Toast.LENGTH_SHORT).show();
            Bridge.getInstance().building = building;
            Bridge.getInstance().gotoFragment(TabActivity.FRAG_Buy);
        }
        else if(v == mLoanConsultText){
            Toast.makeText(getActivity(), "贷款咨询", Toast.LENGTH_SHORT).show();
        }
        else if(v == mPriceText){
            Toast.makeText(getActivity(), "价格切换", Toast.LENGTH_SHORT).show();
        }
        else if(v == mFavBtn || v == mFavText){
            Toast.makeText(getActivity(), "收藏/取消收藏", Toast.LENGTH_SHORT).show();
        }
        else if(v == mThreeTwoDescText){   //新房
            if (building.getIsResale() == 0){
                Toast.makeText(getActivity(), "贷款计算器", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v == mFourTwoDescText){    //二手房
            if (building.getIsResale() > 0){
                Toast.makeText(getActivity(), "贷款计算器", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v == mLocationBtn){
            Toast.makeText(getActivity(), "地图定位", Toast.LENGTH_SHORT).show();
        }
        else if(v == mContactLayout){
            Bridge.getInstance().gotoFragment(TabActivity.FRAG_IntermediaryList);
        }
        else if(v == mContactCallBtn){
            Toast.makeText(getActivity(), "联系联系人", Toast.LENGTH_SHORT).show();
        }
        else if(v == mContactIcon || v == mContactName){
//            Toast.makeText(getActivity(), "角色详情", Toast.LENGTH_SHORT).show();
            Bridge.getInstance().showUser(getActivity(), building.getIntermediary());
        }

    }

    public void initUI(View view) {
        initHeadView(view, this);

        rootView = view.findViewById(R.id.root_view);

        leftImage.setImageResource(R.drawable.fc_nav_back);
        rightImage.setImageResource(R.drawable.fc_nav_back);
        rightImage.setVisibility(View.INVISIBLE);

        discoveryLayout = view.findViewById(R.id.discovery_layout);

        int width = DeviceInfoTool.getScreenWidth(getActivity());
        int height = width*705/1125 + 1;
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(width, height);
        discoveryLayout.setLayoutParams(params);

        mViewPager = view.findViewById(R.id.banner_pager);
        mNumberText = view.findViewById(R.id.banner_number);

        mCallBtn = view.findViewById(R.id.building_bottom_call_icon);
        mCallText = view.findViewById(R.id.building_bottom_call_text);
        mTakeUpBtn = view.findViewById(R.id.building_bottom_takeup_layout);
        mSubscribeBtn = view.findViewById(R.id.building_bottom_subscribe_layout);

        mViewPager = view.findViewById(R.id.banner_pager);
        mNumberText = view.findViewById(R.id.banner_number);
        mTypeImage = view.findViewById(R.id.building_type_bg);
        mTypeText = view.findViewById(R.id.building_type_text);
        mTitleText = view.findViewById(R.id.building_title);
        mPriceText = view.findViewById(R.id.building_total_text);
        mAreaText = view.findViewById(R.id.building_area_text);
        mRoomText = view.findViewById(R.id.building_room_text);
        mFavBtn = view.findViewById(R.id.building_fav_btn);
        mFavText = view.findViewById(R.id.building_fav_text);
        mLoanConsultText = view.findViewById(R.id.building_loan_consult_text);

        mOneOneTitleText = view.findViewById(R.id.building_one_one_title);
        mOneTwoTitleText = view.findViewById(R.id.building_one_two_title);
        mTwoOneTitleText = view.findViewById(R.id.building_two_one_title);
        mTwoTwoTitleText = view.findViewById(R.id.building_two_two_title);
        mThreeOneTitleText = view.findViewById(R.id.building_three_one_title);
        mThreeTwoTitleText = view.findViewById(R.id.building_three_two_title);
        mFourOneTitleText = view.findViewById(R.id.building_four_one_title);
        mFourTwoTitleText = view.findViewById(R.id.building_four_two_title);
        mFiveTitleText = view.findViewById(R.id.building_five_one_title);

        mOneOneDescText = view.findViewById(R.id.building_one_one_desc);
        mOneTwoDescText = view.findViewById(R.id.building_one_two_desc);
        mTwoOneDescText = view.findViewById(R.id.building_two_one_desc);
        mTwoTwoDescText = view.findViewById(R.id.building_two_two_desc);
        mThreeOneDescText = view.findViewById(R.id.building_three_one_desc);
        mThreeTwoDescText = view.findViewById(R.id.building_three_two_desc);
        mFourOneDescText = view.findViewById(R.id.building_four_one_desc);
        mFourTwoDescText = view.findViewById(R.id.building_four_two_desc);
        mFiveDescText = view.findViewById(R.id.building_five_one_desc);

        mLocationBtn = view.findViewById(R.id.building_location);

        mFlowLayout = view.findViewById(R.id.building_flow);

        mContactLayout = view.findViewById(R.id.building_contacts_layout);
        mContactIcon = view.findViewById(R.id.building_contacts_icon);
        mContactName = view.findViewById(R.id.building_contacts_name);
        mContactTypeLayout = view.findViewById(R.id.building_contacts_type_layout);
        mContactTypeText = view.findViewById(R.id.building_contacts_type_title);
        mContactStars = view.findViewById(R.id.building_contacts_stars);
        mContactCompany = view.findViewById(R.id.building_contacts_company);
        mContactCallBtn = view.findViewById(R.id.building_contacts_callbtn);

        mDescText = view.findViewById(R.id.building_desc);
    }

    private void addActions(){

        rootView.setOnClickListener(this);

        mCallBtn.setOnClickListener(this);
        mCallText.setOnClickListener(this);
        mSubscribeBtn.setOnClickListener(this);
        mTakeUpBtn.setOnClickListener(this);

        mPriceText.setOnClickListener(this);
        mFavBtn.setOnClickListener(this);
        mFavText.setOnClickListener(this);

        mLoanConsultText.setOnClickListener(this);

        mThreeTwoDescText.setOnClickListener(this);
        mFourTwoDescText.setOnClickListener(this);
        mLocationBtn.setOnClickListener(this);

        mContactLayout.setOnClickListener(this);
        mContactCallBtn.setOnClickListener(this);
        mContactIcon.setOnClickListener(this);
        mContactName.setOnClickListener(this);
    }

    public void initData(){

        titleText.setText(R.string.building_detail);

        if (Bridge.getInstance().building != null){
            building = Bridge.getInstance().building;
            Bridge.getInstance().building = null;
        }

        mAdapter = new ImageAdapter(getActivity(), mViewPager);
        mViewPager.setAdapter(mAdapter);
        if (building.getImages() != null && building.getImages().size() > 0){
            mAdapter.setData(building.getImages());
            mNumberText.setText("1/" + building.getImages().size());
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mAdapter.setCurrentPos(position);
                    mNumberText.setText((mAdapter.getCurrentPos() + 1)
                            + "/" + building.getImages().size());
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == 0){
                        mAdapter.setTouch(false);
                    }
                    else {
                        mAdapter.setTouch(true);
                    }
                }
            });

        }

        updateUI();
    }

    private void updateUI(){

        mTitleText.setText(building.getTitle());
        mPriceText.setText(AppHelper.getInstance().formatPrice(getActivity(),
                building.getTotalPrice(), getString(R.string.price_total_2)));
        mAreaText.setText(building.getArea() + getString(R.string.area_unit));
        mRoomText.setText(building.getRooms() + getString(R.string.room_unit));
        mFavText.setText(building.getFollowCount() + " "
                + getString(R.string.ren)+ getString(R.string.follower));
        mDescText.setText(building.getDesc());

        if (building.getFeaturesTags() != null && building.getFeaturesTags().size() > 0){
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < building.getFeaturesTags().size(); i++){
                tags.add(building.getFeaturesTags().get(i).getTitle());
            }
            mFlowLayout.setFlowLayout(tags, null);
        }
        else {
            List<String> tags = new ArrayList<>();
            mFlowLayout.setFlowLayout(tags, null);
        }

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
        else {   //未选择中介
            mContactLayout.setVisibility(View.GONE);
        }

        if(building.getIsResale() > 0){  //二手房
            mTypeImage.setVisibility(View.GONE);
            mTypeText.setVisibility(View.GONE);
            mOneOneTitleText.setText(R.string.basic_unitprice);
            mOneTwoTitleText.setText(R.string.basic_type);
            mTwoOneTitleText.setText(R.string.basic_floor);
            mTwoTwoTitleText.setText(R.string.basic_decorate);
            mThreeOneTitleText.setText(R.string.basic_year);
            mThreeTwoTitleText.setText(R.string.basic_orientation);
            mFourOneTitleText.setText(R.string.basic_publish);
            mFourTwoTitleText.setVisibility(View.VISIBLE);
            mFourTwoTitleText.setText(R.string.basic_budget);
            mFiveTitleText.setText(R.string.basic_address);

            mOneOneDescText.setText(AppHelper.getInstance().formatPrice(getActivity(),
                    building.getUnitPrice(), getString(R.string.price_unit_2)));
            mOneTwoDescText.setText(getString(R.string.type_flat));
            mTwoOneDescText.setText(building.getFloor() + getString(R.string.floor));
            mTwoTwoDescText.setText(AppHelper.getInstance().getDecorate(getActivity(), building.getDecorate()));
            mThreeOneDescText.setText(AppHelper.getInstance().stampToDateString(building.getOpenTime(), "yyyy-MM-dd"));
            mThreeTwoDescText.setTextColor(getActivity().getResources().getColor(R.color.color_text));
            mThreeTwoDescText.setText(AppHelper.getInstance().getOrientation(getActivity(), building.getOrientation()));
            mFourOneDescText.setText(AppHelper.getInstance().stampToDateString(building.getCreateAt(), "yyyy-MM-dd"));
            mFourTwoDescText.setVisibility(View.VISIBLE);
            mFourTwoDescText.setText(R.string.loan_and_total);
            mFiveDescText.setText(building.getAddress());
        }
        else {
            mTypeImage.setVisibility(View.VISIBLE);
            mTypeText.setVisibility(View.VISIBLE);
            mOneOneTitleText.setText(R.string.basic_type);
            mOneTwoTitleText.setText(R.string.basic_status);
            mTwoOneTitleText.setText(R.string.basic_floor);
            mTwoTwoTitleText.setText(R.string.basic_decorate);
            mThreeOneTitleText.setText(R.string.basic_publish);
            mThreeTwoTitleText.setText(R.string.basic_budget);
            mFourOneTitleText.setText(R.string.basic_opentime);
            mFourTwoTitleText.setVisibility(View.GONE);
            mFiveTitleText.setText(R.string.basic_address);

            mOneOneDescText.setText(getString(R.string.type_flat));
            mOneTwoDescText.setText(AppHelper.getInstance().getStatus(getActivity(),
                    building.getStatus()));;
            mTwoOneDescText.setText(building.getFloor() + getString(R.string.floor));
            mTwoTwoDescText.setText(AppHelper.getInstance().getDecorate(getActivity(),
                    building.getDecorate()));
            mThreeOneDescText.setText(AppHelper.getInstance().stampToDateString(
                    building.getCreateAt(), "yyyy-MM-dd"));
            mThreeTwoDescText.setTextColor(getActivity().getResources().getColor(R.color.color_link));
            mThreeTwoDescText.setText(R.string.loan_and_total);
            mFourOneDescText.setText(AppHelper.getInstance().stampToDateString(
                    building.getOpenTime(), "yyyy-MM-dd"));
            mFourTwoDescText.setVisibility(View.GONE);
            mFiveDescText.setText(building.getAddress());
        }
    }
}
