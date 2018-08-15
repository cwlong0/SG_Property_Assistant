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
import com.softgrid.shortvideo.tool.DatePopView;
import com.softgrid.shortvideo.tool.DeviceInfoTool;
import com.softgrid.shortvideo.utils.AppHelper;


/**
 * Created by tianfeng on 2018/7/16.
 */

public class FragmentBespoke extends BaseFragment {

    private View rootView;

    private DatePopView datePopView;

    private View mBespokeBtn;

    private ImageView mBesBuildingImage;
    private TextView mBesBuildingTitle;
    private TextView mBesBuildingDesc;

    private View mContactLayout;
    private ImageView mContactIcon;
    private TextView mContactName;
    private View mContactTypeLayout;
    private TextView mContactTypeText;
    private ImageView mContactStars;
    private TextView mContactCompany;
    private CImageView mContactCallBtn;

    private View mBespokeTimeLayout;
    private TextView mBespokeTimeText;

    private Building building;
    private long bespokeTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.frag_bespoke, container, false);
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
        else if(v == mBespokeBtn){
            Toast.makeText(getActivity(), "预约", Toast.LENGTH_SHORT).show();
        }
        else if(v == mBesBuildingImage || v == mBesBuildingTitle || v == mBesBuildingDesc){
            Bridge.getInstance().showBuilding(getActivity(), building);
        }
        else if(v == mContactLayout){
            Bridge.getInstance().gotoFragment(TabActivity.FRAG_IntermediaryList);
        }
        else if(v == mContactCallBtn){
            Toast.makeText(getActivity(), "联系", Toast.LENGTH_SHORT).show();
        }
        else if(v == mContactIcon || v == mContactName){
            Bridge.getInstance().showUser(getActivity(), building.getIntermediary());
        }
        else if(v == mBespokeTimeLayout){
            showChooseDatePop();
        }

    }

    public void initUI(View view) {
        initHeadView(view, this);

        leftImage.setImageResource(R.drawable.fc_nav_back);
        rightImage.setImageResource(R.drawable.fc_nav_back);
        rightImage.setVisibility(View.INVISIBLE);

        rootView = view.findViewById(R.id.root_view);

        mBespokeBtn = view.findViewById(R.id.bespoke_layout);

        mBesBuildingImage = view.findViewById(R.id.building_image);
        mBesBuildingTitle = view.findViewById(R.id.building_title);
        mBesBuildingDesc = view.findViewById(R.id.building_desc);

        mContactLayout = view.findViewById(R.id.building_contacts_layout);
        mContactIcon = view.findViewById(R.id.building_contacts_icon);
        mContactName = view.findViewById(R.id.building_contacts_name);
        mContactTypeLayout = view.findViewById(R.id.building_contacts_type_layout);
        mContactTypeText = view.findViewById(R.id.building_contacts_type_title);
        mContactStars = view.findViewById(R.id.building_contacts_stars);
        mContactCompany = view.findViewById(R.id.building_contacts_company);
        mContactCallBtn = view.findViewById(R.id.building_contacts_callbtn);

        mBespokeTimeLayout = view.findViewById(R.id.bespoke_time_layout);
        mBespokeTimeText = view.findViewById(R.id.bespoke_time_text);

        int width = DeviceInfoTool.getScreenWidth(getActivity());
        int height = width*705/1125 + 1;
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(width, height);
        mBesBuildingImage.setLayoutParams(params);
    }

    private void addActions(){

        rootView.setOnClickListener(this);

        mBesBuildingImage.setOnClickListener(this);
        mBesBuildingTitle.setOnClickListener(this);
        mBesBuildingDesc.setOnClickListener(this);

        mBespokeBtn.setOnClickListener(this);
        mContactLayout.setOnClickListener(this);
        mContactCallBtn.setOnClickListener(this);
        mContactName.setOnClickListener(this);
        mContactIcon.setOnClickListener(this);
        mBespokeTimeLayout.setOnClickListener(this);

    }

    public void initData(){

        titleText.setText(R.string.bespoke_see);

        if (Bridge.getInstance().building != null){
            building = Bridge.getInstance().building;
            Bridge.getInstance().building = null;
        }

        bespokeTime = System.currentTimeMillis();

        updateUI();
    }


    private void showChooseDatePop(){

        if (datePopView == null){
            datePopView = new DatePopView(getActivity(), new DatePopView.DatePopListener(){

                @Override
                public void onCancel() {

                }

                @Override
                public void onConfirm(long time) {
                    bespokeTime = time;
                    updateUI();
                }
            });
        }
        datePopView.show(rootView);
    }

    private void updateUI(){

        mBespokeTimeText.setText(AppHelper.getInstance().
                stampToDateString(bespokeTime, "yyyy-MM-dd"));

        mBesBuildingTitle.setText(building.getTitle());
        Glide.with(getActivity().getApplicationContext())
                .load(Uri.parse(building.getOriginal()))
                .apply(AppHelper.getInstance().getBannerOptions())
                .into(mBesBuildingImage);
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
        mBesBuildingDesc.setText(descText);

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
    }
}
