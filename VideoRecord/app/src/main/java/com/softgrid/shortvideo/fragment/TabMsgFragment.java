package com.softgrid.shortvideo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softgrid.shortvideo.R;

/**
 * Created by tianfeng on 2017/12/10.
 */

public class TabMsgFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.tab_msg, container, false);
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

    }

    public void initUI(View view) {
        initHeadView(view, this);
    }

    private void addActions(){

    }

    public void initData(){
        titleText.setText(R.string.tab_3);
    }

    private void updateUI(){

    }
}
