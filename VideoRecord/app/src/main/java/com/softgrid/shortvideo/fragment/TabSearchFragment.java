package com.softgrid.shortvideo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.adapter.BuildingAdapter;
import com.softgrid.shortvideo.customView.CImageView;
import com.softgrid.shortvideo.customView.FlowLayout;
import com.softgrid.shortvideo.model.Building;

import java.util.ArrayList;

/**
 * Created by tianfeng on 2017/12/10.
 */

public class TabSearchFragment extends Fragment {

    private EditText mInputText;
    private CImageView mDelBtn;
    private CImageView mSearchBtn;

    private View mFlowLayout;
    private FlowLayout mHotFlowLayout;
    private FlowLayout mHisFlowLayout;
    private View mHisClearBtn;
    private ListView mKeywordListView;

    private View mLeftView;
    private View mRightView;
    private TextView mLeftText;
    private TextView mRightText;

    private View mPromptView;

    private PullToRefreshListView mListView;

    private BuildingAdapter buildingAdapter;

    private String currentKeyword;
    private String searchKeyword;

    private int currentPage;

    private boolean isLoading;

    private ArrayList<Building> buildingList;

    private ArrayList<String> hotWordList;
    private ArrayList<String> hisWordList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.tab_search, container, false);
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


    public void initUI(View view) {

        mInputText = view.findViewById(R.id.input_box);
        mDelBtn = view.findViewById(R.id.del_icon);
        mSearchBtn = view.findViewById(R.id.search_ico);

        mFlowLayout = view.findViewById(R.id.flow_layout);
        mHotFlowLayout = view.findViewById(R.id.flow_hot);
        mHisFlowLayout = view.findViewById(R.id.flow_his);
        mHisClearBtn = view.findViewById(R.id.his_clear_text);
        mKeywordListView = view.findViewById(R.id.keyword_list);

        mLeftView = view.findViewById(R.id.switch_left_layout);
        mRightView = view.findViewById(R.id.switch_right_layout);
        mLeftText = view.findViewById(R.id.switch_left_text);
        mRightText = view.findViewById(R.id.switch_right_text);


        mListView = view.findViewById(R.id.data_list);


        mPromptView = view.findViewById(R.id.prompt_layout);
    }

    private void addActions(){

    }

    public void initData(){

        buildingList = new ArrayList<>();

        hotWordList = new ArrayList<>();
        hisWordList = new ArrayList<>();

        currentKeyword = "";
        searchKeyword = "";
        currentPage = 1;

    }

    private void updateUI(){

    }
}
