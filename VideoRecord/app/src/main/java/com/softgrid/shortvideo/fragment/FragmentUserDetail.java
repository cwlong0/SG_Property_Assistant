package com.softgrid.shortvideo.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.customView.CImageView;
import com.softgrid.shortvideo.customView.FlowLayout;
import com.softgrid.shortvideo.info.Bridge;
import com.softgrid.shortvideo.model.User;
import com.softgrid.shortvideo.utils.AppHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianfeng on 2017/12/10.
 */

public class FragmentUserDetail extends BaseFragment {


    private View rootView;

    private View mCallBtn;                     //咨询按钮
    private View mCallText;                    //咨询text
    private View mWechatBtn;                   //微信按钮

    private ImageView mUserIcon;
    private TextView mUserName;
    private ImageView mUserStars;
    private TextView mUserCompany;
    private FlowLayout mUserFlowLayout;
    private FlowLayout mUserBusinessFlowLayout;
    private TextView mUserIntro;

    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.frag_user, container, false);
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

        }
        else if(v == mWechatBtn){

        }

    }

    public void initUI(View view) {
        initHeadView(view, this);

        rootView = view.findViewById(R.id.root_view);

        leftImage.setImageResource(R.drawable.fc_nav_back);
        rightImage.setImageResource(R.drawable.fc_nav_back);
        rightImage.setVisibility(View.INVISIBLE);

        mCallBtn = view.findViewById(R.id.user_bottom_call_icon);
        mCallText = view.findViewById(R.id.user_bottom_call_text);
        mWechatBtn = view.findViewById(R.id.user_wechat_layout);

        mUserIcon = view.findViewById(R.id.user_icon);
        mUserName = view.findViewById(R.id.user_name);
        mUserStars = view.findViewById(R.id.user_stars);
        mUserCompany = view.findViewById(R.id.user_company);

        mUserFlowLayout = view.findViewById(R.id.user_flow);
        mUserBusinessFlowLayout = view.findViewById(R.id.user_business_flow);
        mUserIntro = view.findViewById(R.id.user_desc);

    }

    private void addActions(){
        rootView.setOnClickListener(this);

        mCallBtn.setOnClickListener(this);
        mCallText.setOnClickListener(this);
        mWechatBtn.setOnClickListener(this);
    }

    public void initData(){

        titleText.setText(R.string.detail);

        if (Bridge.getInstance().user != null){
            user = Bridge.getInstance().user;
            Bridge.getInstance().user = null;
        }

        updateUI();


    }

    private void updateUI(){
        mUserName.setText(user.getName());
        mUserCompany.setText(user.getCompany());
        mUserIntro.setText(user.getIntro());

        if (user.getImage() != null){
            Glide.with(getActivity().getApplicationContext())
                    .load(Uri.parse(user.getImage()))
                    .apply(AppHelper.getInstance().getUserOptions())
                    .into(mUserIcon);
        }

        if (user.getTags() != null && user.getTags().size() > 0){
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < user.getTags().size(); i++){
                tags.add(user.getTags().get(i).getTitle());
            }
            mUserFlowLayout.setFlowLayout(tags, null);
        }
        else {
            List<String> tags = new ArrayList<>();
            mUserFlowLayout.setFlowLayout(tags, null);
        }

        if (user.getBusiness() != null && user.getBusiness().size() > 0){
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < user.getBusiness().size(); i++){
                tags.add(user.getBusiness().get(i).getTitle());
            }
            mUserBusinessFlowLayout.setFlowLayout(tags, null);
        }
        else {
            List<String> tags = new ArrayList<>();
            mUserBusinessFlowLayout.setFlowLayout(tags, null);
        }

        if (user.getRate() < 20){
            mUserStars.setImageResource(R.drawable.fc_stars_01);
        }
        else if (user.getRate() < 30){
            mUserStars.setImageResource(R.drawable.fc_stars_02);
        }
        else if (user.getRate() < 40){
            mUserStars.setImageResource(R.drawable.fc_stars_03);
        }
        else if (user.getRate() < 50){
            mUserStars.setImageResource(R.drawable.fc_stars_04);
        }
        else {
            mUserStars.setImageResource(R.drawable.fc_stars_05);
        }
    }
}
