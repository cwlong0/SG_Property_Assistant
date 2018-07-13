package com.softgrid.shortvideo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.info.Bridge;

/**
 * Created by tianfeng on 2017/12/10.
 */

public class FragmentIntermediaryDetail extends BaseFragment {


    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.frag_intermediary_list, container, false);
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

    }

    public void initUI(View view) {
        initHeadView(view, this);

        rootView = view.findViewById(R.id.root_view);

        leftImage.setImageResource(R.drawable.fc_nav_back);
        rightImage.setImageResource(R.drawable.fc_nav_back);
        rightImage.setVisibility(View.INVISIBLE);


    }

    private void addActions(){
        rootView.setOnClickListener(this);
    }

    public void initData(){
        titleText.setText(R.string.intermediary_recommend);


    }

    private void updateUI(){

    }
}
