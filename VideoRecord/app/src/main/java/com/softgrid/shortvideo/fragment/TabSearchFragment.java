package com.softgrid.shortvideo.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.adapter.BuildingAdapter;
import com.softgrid.shortvideo.adapter.KeywordAdapter;
import com.softgrid.shortvideo.adapter.PopListAdapter;
import com.softgrid.shortvideo.callback.CallBackListener;
import com.softgrid.shortvideo.callback.ErrorInfo;
import com.softgrid.shortvideo.customView.CImageView;
import com.softgrid.shortvideo.customView.FlowLayout;
import com.softgrid.shortvideo.factory.LogicFactory;
import com.softgrid.shortvideo.info.Bridge;
import com.softgrid.shortvideo.logicImpl.LocalApi;
import com.softgrid.shortvideo.model.Building;
import com.softgrid.shortvideo.model.HotWord;
import com.softgrid.shortvideo.model.SearchCondition;
import com.softgrid.shortvideo.tool.DeviceInfoTool;
import com.softgrid.shortvideo.tool.DeviceTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianfeng on 2017/12/10.
 */

public class TabSearchFragment extends Fragment implements View.OnClickListener{

    private View rootView;

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

    private TextView mConRegionText;
    private TextView mConRegionTriangle;
    private TextView mConPriceText;
    private TextView mConPriceTriangle;
    private TextView mConTypeText;
    private TextView mConTypeTriangle;
    private TextView mConMoreText;
    private TextView mConMoreTriangle;

    private View mPopLineView;

    private View mPromptView;

    private PullToRefreshListView mListView;

    private View mMaskView;

    private PopupWindow mRegionPop;
    private View mRegionBgView;
    private ListView mRegionPopListView1;
    private ListView mRegionPopListView2;
    private PopListAdapter mRegionPopListAdapter1;
    private PopListAdapter mRegionPopListAdapter2;

    private KeywordAdapter keywordAdapter;
    private BuildingAdapter buildingAdapter;

    private String currentKeyword;
    private String searchKeyword;

    private SearchCondition condition;

    private int currentPage;

    private boolean isLoading;

    private ArrayList<Building> buildingList;

    private ArrayList<HotWord> hotWordList;
    private ArrayList<String> hisWordList;

    private int currentTab;


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
        if (hotWordList.size() == 0){
            getHotWordData();
        }
        getHisWordData();
        updateUI();
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (hotWordList.size() == 0){
                getHotWordData();
            }
            updateUI();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == rootView){

        }
        else if(v == mSearchBtn){
            search();
        }
        else if(v == mDelBtn){
            getHisWordData();
            clearKey();
        }
        else if(v == mHisClearBtn){
            clearHis();
        }

        else if(v == mInputText){
            if (currentKeyword != null && !currentKeyword.equals("")){
                getKeywordData();
            }
            else{
                mFlowLayout.setVisibility(View.VISIBLE);
                getHisWordData();
            }
        }
        else if(v == mLeftView){
            switchTab(1);
        }

        else if(v == mRightView){
            switchTab(2);
        }
        else if(v == mConRegionText || v == mConRegionTriangle){
            regionPop();
        }
        else if(v == mConPriceText || v == mConPriceTriangle){
            pricePop();
        }
        else if(v == mConTypeText || v == mConTypeTriangle){
            typePop();
        }
        else if(v == mConMoreText || v == mConMoreTriangle){
            morePop();
        }
        else if(v == mRegionBgView){
            if (mRegionPop.isShowing()){
                mRegionPop.dismiss();
            }
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

        mConRegionText = view.findViewById(R.id.con_region_text);
        mConRegionTriangle = view.findViewById(R.id.con_region_triangle);
        mConPriceText = view.findViewById(R.id.con_price_text);
        mConPriceTriangle = view.findViewById(R.id.con_price_triangle);
        mConTypeText = view.findViewById(R.id.con_type_text);
        mConTypeTriangle = view.findViewById(R.id.con_type_triangle);
        mConMoreText = view.findViewById(R.id.con_more_text);
        mConMoreTriangle = view.findViewById(R.id.con_more_triangle);

        mPopLineView = view.findViewById(R.id.pop_line_view);
        mMaskView = view.findViewById(R.id.mask_view);

        mListView = view.findViewById(R.id.data_list);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);


        mPromptView = view.findViewById(R.id.prompt_layout);
    }

    private void addActions(){

        mDelBtn.setOnClickListener(this);
        mSearchBtn.setOnClickListener(this);
        mLeftView.setOnClickListener(this);
        mRightView.setOnClickListener(this);

        mConRegionText.setOnClickListener(this);
        mConRegionTriangle.setOnClickListener(this);
        mConPriceText.setOnClickListener(this);
        mConPriceTriangle.setOnClickListener(this);
        mConTypeText.setOnClickListener(this);
        mConTypeTriangle.setOnClickListener(this);
        mConMoreText.setOnClickListener(this);
        mConMoreTriangle.setOnClickListener(this);

        mHisClearBtn.setOnClickListener(this);

        mKeywordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DeviceTool.getInstance().hideKeybord(mInputText);

                currentKeyword = keywordAdapter.getItem(position);
                mInputText.setText(currentKeyword);
                mInputText.setSelection(currentKeyword.length());
                search();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Bridge.getInstance().showBuilding(getActivity(),
                        buildingAdapter.getItem(position - 1));
            }
        });

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(false);
            }
        });

        mInputText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 66 && event.getAction() == KeyEvent.ACTION_DOWN){ //回车键被按下
                    search();
                    return true;
                }
                return false;
            }
        });

        mInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentKeyword = editable.toString();
                getKeywordData();
                if (currentKeyword == null || currentKeyword.equals("")){
                    mKeywordListView.setVisibility(View.GONE);
                    mDelBtn.setVisibility(View.GONE);
                    mFlowLayout.setVisibility(View.VISIBLE);
                }
                else {
                    mDelBtn.setVisibility(View.VISIBLE);
                    mFlowLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    public void initData(){

        buildingList = new ArrayList<>();

        hotWordList = new ArrayList<>();
        hisWordList = new ArrayList<>();

        currentKeyword = "";
        searchKeyword = "";
        currentPage = 1;
        condition = new SearchCondition();

        keywordAdapter = new KeywordAdapter(getActivity());
        mKeywordListView.setAdapter(keywordAdapter);

        buildingAdapter = new BuildingAdapter(getActivity());
        mListView.setAdapter(buildingAdapter);

        switchTab(1);

    }

    private void updateUI(){

    }

    private void regionPop(){

        if (mRegionPop == null){
            View popView = getActivity().getLayoutInflater().inflate(R.layout.pop_region, null);
            int width = DeviceInfoTool.getScreenWidth(getActivity());
            int height = DeviceInfoTool.getScreenHeight(getActivity());

            mRegionPop = new PopupWindow(popView, width, height, true);

            mRegionPop.setBackgroundDrawable(new ColorDrawable());
            mRegionPop.setOutsideTouchable(true);

            mRegionBgView = popView.findViewById(R.id.root_view);
            mRegionPopListView1 = popView.findViewById(R.id.region_list);
            mRegionPopListView2 = popView.findViewById(R.id.pd_list);

            mRegionPopListView1.setVerticalScrollBarEnabled(false);
            mRegionPopListView2.setVerticalScrollBarEnabled(false);

            mRegionBgView.setOnClickListener(this);
            mRegionPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mMaskView.setVisibility(View.GONE);
                }
            });

            mRegionPopListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    condition.setPd(position);
                    search();

                    String text = condition.getPd()
                            + getActivity().getResources().getString(R.string.region);

                    mRegionPopListAdapter1.setKeyword(text);
                    mRegionPop.dismiss();
                    if (condition.getPd() > 0){
                        mConRegionText.setText(text);
                        mConRegionTriangle.setText(getString(R.string.con_triangle_up));
                        mConRegionText.setTextColor(getActivity().getColor(R.color.main_top));
                        mConRegionTriangle.setTextColor(getActivity().getColor(R.color.main_top));
                    }
                    else {
                        mConRegionText.setText(getActivity().getResources().getString(R.string.con_region));
                        mConRegionTriangle.setText(getString(R.string.con_triangle));
                        mConRegionText.setTextColor(getActivity().getColor(R.color.color_detail));
                        mConRegionTriangle.setTextColor(getActivity().getColor(R.color.color_text2));
                    }
                }
            });

            mRegionPopListAdapter1 = new PopListAdapter(getActivity());
            mRegionPopListView1.setAdapter(mRegionPopListAdapter1);
            mRegionPopListAdapter2 = new PopListAdapter(getActivity());
            mRegionPopListView2.setAdapter(mRegionPopListAdapter2);

            ArrayList<String> regionList = new ArrayList<>();
            regionList.add(getActivity().getResources().getString(R.string.unlimited));
            for (int i = 1; i < 29; i++){
                regionList.add(i + getActivity().getResources().getString(R.string.region));
            }
            mRegionPopListAdapter1.setData(regionList);
        }

        if (mRegionPop.isShowing()){
            mRegionPop.dismiss();
            return;
        }
        if (condition.getPd() > 0){
            mRegionPopListAdapter1.setKeyword(condition.getPd()
                    + getActivity().getResources().getString(R.string.region));
        }
        else {
            mRegionPopListAdapter1.setKeyword(null);
        }
        mMaskView.setVisibility(View.VISIBLE);
        mRegionPop.showAsDropDown(mPopLineView, Gravity.BOTTOM, 0, 0);

    }

    private void pricePop(){

    }

    private void typePop(){

    }

    private void morePop(){

    }

    private void clearHis(){
        LocalApi.getInstance(getActivity()).clearKeyWordHis(getActivity());
        getHisWordData();
    }

    private void clearKey(){
        currentKeyword = "";
        mInputText.setText(currentKeyword);
    }

    private void switchTab(int tab){

        if (currentTab == tab){
            return;
        }
        currentTab = tab;

        mLeftView.setSelected(false);
        mLeftText.setSelected(false);

        mRightView.setSelected(false);
        mRightText.setSelected(false);

        switch (currentTab){
            case 1:
                mLeftView.setSelected(true);
                mLeftText.setSelected(true);
                break;

            case 2:
                mRightView.setSelected(true);
                mRightText.setSelected(true);
                break;
        }

        search();

    }

    private void getHisWordData(){

        List<String> list = LocalApi.getInstance(getActivity()).getKeyWordHis(getActivity());
        hisWordList.clear();
        if (list != null){
            hisWordList.addAll(list);
        }

        mHisFlowLayout.setFlowLayout(hisWordList, new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(String content) {
                currentKeyword = content;
                mInputText.setText(currentKeyword);
                mInputText.setSelection(currentKeyword.length());
                search();
            }
        });
    }

    private void getHotWordData(){
        LogicFactory.getInstance().getNetLogic().getHotWords(getActivity(),
                new CallBackListener<ArrayList<HotWord>>() {
                    @Override
                    public void onCache(ArrayList<HotWord> data) {

                    }

                    @Override
                    public void onComplete(ArrayList<HotWord> data) {
                        hotWordList.clear();
                        if (data != null){
                            hotWordList.addAll(data);
                        }
                        ArrayList<String> dataList = new ArrayList<>();
                        for (int i = 0; i < hotWordList.size(); i++){
                            dataList.add(hotWordList.get(i).getWord());
                        }

                        mHotFlowLayout.setFlowLayout(dataList, new FlowLayout.OnItemClickListener() {
                            @Override
                            public void onItemClick(String content) {
                                currentKeyword = content;
                                mInputText.setText(currentKeyword);
                                mInputText.setSelection(currentKeyword.length());
                                search();
                            }
                        });
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {

                    }
                });
    }

    private void getKeywordData(){

        mKeywordListView.setVisibility(View.VISIBLE);

        LogicFactory.getInstance().getNetLogic().searchThink(getActivity(), currentKeyword, new CallBackListener<ArrayList<String>>() {
            @Override
            public void onCache(ArrayList<String> data) {

            }

            @Override
            public void onComplete(ArrayList<String> data) {
                keywordAdapter.setKeyword(currentKeyword);
                keywordAdapter.setData(data);
            }

            @Override
            public void onError(ErrorInfo errorInfo) {

            }
        });

    }

    private void search(){

        searchKeyword = currentKeyword;

        if (currentKeyword != null && !currentKeyword.equals("")){

            LocalApi.getInstance(getActivity()).addKeyWordHis(getActivity(), currentKeyword);

            DeviceTool.getInstance().hideKeybord(mInputText);
            mKeywordListView.setVisibility(View.GONE);
            mFlowLayout.setVisibility(View.GONE);

            mListView.setRefreshing();
        }
    }

    private void loadData(boolean reload){
        if (isLoading){
            return;
        }
        if (searchKeyword == null){
            mListView.onRefreshComplete();
            return;
        }

        if (reload){
            buildingList.clear();
            buildingAdapter.setItemsData(buildingList);
            mPromptView.setVisibility(View.GONE);

            currentPage = 1;
        }
        else {
            currentPage++;
        }

        isLoading = true;

        LogicFactory.getInstance().getNetLogic().searchBuilding(getActivity(), searchKeyword, currentPage, condition,
                new CallBackListener<ArrayList<Building>>() {
                    @Override
                    public void onCache(ArrayList<Building> data) {

                    }

                    @Override
                    public void onComplete(ArrayList<Building> data) {
                        isLoading = false;
                        mListView.onRefreshComplete();

                        if (data != null){
                            buildingList.addAll(data);
                            buildingAdapter.setItemsData(buildingList);
                        }

                        if (buildingList.size() == 0){
                            mPromptView.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        isLoading = false;
                        mListView.onRefreshComplete();
                        if (buildingList.size() == 0){
                            mPromptView.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }
}
