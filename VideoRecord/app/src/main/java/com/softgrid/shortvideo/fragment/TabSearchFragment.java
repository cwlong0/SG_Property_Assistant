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

    private PopupWindow mPricePop;
    private View mPriceBgView;
    private ListView mPricePopListView;
    private PopListAdapter mPricePopListAdapter;
    private ArrayList<String> priceList;

    private PopupWindow mTypePop;
    private View mTypeBgView;
    private ListView mTypePopListView;
    private PopListAdapter mTypePopListAdapter;
    private ArrayList<String> typeList;

    private PopupWindow mMorePop;
    private View mMoreBgView;
    private FlowLayout mAreaFlow;
    private FlowLayout mOrientationFlow;
    private FlowLayout mDecorateFlow;
    private FlowLayout mSortFlow;
    private View mMoreClearBtn;
    private View mMoreConfirmBtn;

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
        else if(v == mPriceBgView){
            if (mPricePop.isShowing()){
                mPricePop.dismiss();
            }
        }
        else if(v == mTypeBgView){
            if (mTypePop.isShowing()){
                mTypePop.dismiss();
            }
        }
        else if(v == mMoreBgView){
            if (mMorePop.isShowing()){
                mMorePop.dismiss();
            }
        }
        else if(v == mMoreClearBtn){
            mAreaFlow.setSelectedItem(null);
            mDecorateFlow.setSelectedItem(null);
            mOrientationFlow.setSelectedItem(null);
            mSortFlow.setSelectedItem(null);
            condition.setArea(null);
            condition.setOrientation(0);
            condition.setDecorate(0);
            condition.setSort(0);
            updateUI();
        }
        else if(v == mMoreConfirmBtn){
            if (mMorePop.isShowing()){
                mMorePop.dismiss();
            }
            updateUI();
            search();
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

        mFlowLayout.setOnClickListener(this);

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

        if (mRegionPop != null){
            String text = condition.getPd()
                    + getActivity().getResources().getString(R.string.region);

            mRegionPopListAdapter1.setKeyword(text);
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

        if (mPricePop != null){
            String text = null;
            if (condition.getTotalPrice() != null){
                text = condition.getTotalPrice()
                        + getActivity().getResources().getString(R.string.wan);

                if (condition.getTotalPrice().equals("200")){
                    text = condition.getTotalPrice()
                            + getActivity().getResources().getString(R.string.wan)
                            + getString(R.string.up);
                }
            }

            mPricePopListAdapter.setKeyword(text);

            if (condition.getTotalPrice() != null){
                mConPriceText.setText(text);
                mConPriceTriangle.setText(getString(R.string.con_triangle_up));
                mConPriceText.setTextColor(getActivity().getColor(R.color.main_top));
                mConPriceTriangle.setTextColor(getActivity().getColor(R.color.main_top));
            }
            else {
                mConPriceText.setText(getActivity().getResources().getString(R.string.con_price));
                mConPriceTriangle.setText(getString(R.string.con_triangle));
                mConPriceText.setTextColor(getActivity().getColor(R.color.color_detail));
                mConPriceTriangle.setTextColor(getActivity().getColor(R.color.color_text2));
            }
        }

        if (mTypePop != null){
            String text = null;
            if (condition.getRooms() != null){
                text = condition.getRooms()
                        + getActivity().getResources().getString(R.string.room_unit);

                if (condition.getRooms().equals("6")){
                    text = "5"
                            + getString(R.string.room_unit)
                            + getString(R.string.up);
                }
            }

            mTypePopListAdapter.setKeyword(text);

            if (condition.getRooms() != null){
                mConTypeText.setText(text);
                mConTypeTriangle.setText(getString(R.string.con_triangle_up));
                mConTypeText.setTextColor(getActivity().getColor(R.color.main_top));
                mConTypeTriangle.setTextColor(getActivity().getColor(R.color.main_top));
            }
            else {
                mConTypeText.setText(getActivity().getResources().getString(R.string.con_type));
                mConTypeTriangle.setText(getString(R.string.con_triangle));
                mConTypeText.setTextColor(getActivity().getColor(R.color.color_detail));
                mConTypeTriangle.setTextColor(getActivity().getColor(R.color.color_text2));
            }
        }

        if (mMorePop != null){
            String text = null;
            if (condition.getArea() != null){

                if (condition.getArea().equals("0-50")){
                    text = "50" + getString(R.string.area_unit) + getString(R.string.down);
                    condition.setArea("0-50");
                }
                else if(condition.getArea().equals("50-70")){
                    text = "50 - 70" + getString(R.string.area_unit);
                }
                else if(condition.getArea().equals("70-90")){
                    text = "70 - 90" + getString(R.string.area_unit);
                }
                else if(condition.getArea().equals("90-120")){
                    text = "90 - 120" + getString(R.string.area_unit);
                }
                else if(condition.getArea().equals("120")){
                    text = "120" + getString(R.string.area_unit) + getString(R.string.up);
                }
            }

            if (condition.getOrientation() > 0){
                if (condition.getOrientation() == 1){
                    if (text == null){
                        text = getString(R.string.orientation_1);
                    }
                    else {
                        text = text + "," + getString(R.string.orientation_1);
                    }
                }
                else if(condition.getOrientation() == 2){
                    if (text == null){
                        text = getString(R.string.orientation_2);
                    }
                    else {
                        text = text + "," + getString(R.string.orientation_2);
                    }
                }
                else if(condition.getOrientation() == 3){
                    if (text == null){
                        text = getString(R.string.orientation_3);
                    }
                    else {
                        text = text + "," + getString(R.string.orientation_3);
                    }
                }
                else if(condition.getOrientation() == 4){
                    if (text == null){
                        text = getString(R.string.orientation_4);
                    }
                    else {
                        text = text + "," + getString(R.string.orientation_4);
                    }
                }
                else if(condition.getOrientation() == 5){
                    if (text == null){
                        text = getString(R.string.orientation_5);
                    }
                    else {
                        text = text + "," + getString(R.string.orientation_5);
                    }
                }
                else if(condition.getOrientation() == 6){
                    if (text == null){
                        text = getString(R.string.orientation_6);
                    }
                    else {
                        text = text + "," + getString(R.string.orientation_6);
                    }
                }
                else if(condition.getOrientation() == 7){
                    if (text == null){
                        text = getString(R.string.orientation_7);
                    }
                    else {
                        text = text + "," + getString(R.string.orientation_7);
                    }
                }
                else if(condition.getOrientation() == 8){
                    if (text == null){
                        text = getString(R.string.orientation_8);
                    }
                    else {
                        text = text + "," + getString(R.string.orientation_8);
                    }
                }
            }

            if (condition.getDecorate() > 0){

                if (condition.getDecorate() == 1){
                    if (text == null){
                        text = getString(R.string.decorate_1);
                    }
                    else {
                        text = text + "," + getString(R.string.decorate_1);
                    }
                }
                else if (condition.getDecorate() == 2){
                    if (text == null){
                        text = getString(R.string.decorate_2);
                    }
                    else {
                        text = text + "," + getString(R.string.decorate_2);
                    }
                }
                else if (condition.getDecorate() == 3){
                    if (text == null){
                        text = getString(R.string.decorate_3);
                    }
                    else {
                        text = text + "," + getString(R.string.decorate_3);
                    }
                }
                else if (condition.getDecorate() == 4){
                    if (text == null){
                        text = getString(R.string.decorate_4);
                    }
                    else {
                        text = text + "," + getString(R.string.decorate_4);
                    }
                }
                else if (condition.getDecorate() == 5){
                    if (text == null){
                        text = getString(R.string.decorate_5);
                    }
                    else {
                        text = text + "," + getString(R.string.decorate_5);
                    }
                }
            }

            if (condition.getSort() > 0){
                if (condition.getSort() == 1){
                    if (text == null){
                        text = getString(R.string.sort_price);
                    }
                    else {
                        text = text + "," + getString(R.string.sort_price);
                    }
                }
                else if (condition.getSort() == 2){
                    if (text == null){
                        text = getString(R.string.sort_publish);
                    }
                    else {
                        text = text + "," + getString(R.string.sort_publish);
                    }
                }
                else if (condition.getSort() == 3){
                    if (text == null){
                        text = getString(R.string.sort_open);
                    }
                    else {
                        text = text + "," + getString(R.string.sort_open);
                    }
                }
                else if (condition.getSort() == 4){
                    if (text == null){
                        text = getString(R.string.sort_area);
                    }
                    else {
                        text = text + "," + getString(R.string.sort_area);
                    }
                }
            }

            if (text != null){
                mConMoreText.setText(text);
                mConMoreTriangle.setText(getString(R.string.con_triangle_up));
                mConMoreText.setTextColor(getActivity().getColor(R.color.main_top));
                mConMoreTriangle.setTextColor(getActivity().getColor(R.color.main_top));
            }
            else {
                mConMoreText.setText(getActivity().getResources().getString(R.string.con_more));
                mConMoreTriangle.setText(getString(R.string.con_triangle));
                mConMoreText.setTextColor(getActivity().getColor(R.color.color_detail));
                mConMoreTriangle.setTextColor(getActivity().getColor(R.color.color_text2));
            }
        }

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
                    mRegionPop.dismiss();
                    updateUI();
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

        if (mPricePop == null){
            View popView = getActivity().getLayoutInflater().inflate(R.layout.pop_price, null);
            int width = DeviceInfoTool.getScreenWidth(getActivity());
            int height = DeviceInfoTool.getScreenHeight(getActivity());

            mPricePop = new PopupWindow(popView, width, height, true);

            mPricePop.setBackgroundDrawable(new ColorDrawable());
            mPricePop.setOutsideTouchable(true);

            mPriceBgView = popView.findViewById(R.id.root_view);
            mPricePopListView = popView.findViewById(R.id.price_list);

            mPricePopListView.setVerticalScrollBarEnabled(false);

            mPriceBgView.setOnClickListener(this);
            mPricePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mMaskView.setVisibility(View.GONE);
                }
            });

            mPricePopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    if (position > 0){
                        condition.setTotalPrice(priceList.get(position));
                    }
                    else {
                        condition.setTotalPrice(null);
                    }

                    search();
                    mPricePop.dismiss();
                    updateUI();
                }
            });

            mPricePopListAdapter = new PopListAdapter(getActivity());
            mPricePopListView.setAdapter(mPricePopListAdapter);

            priceList = new ArrayList<>();
            priceList.add("0");
            priceList.add("0-30");
            priceList.add("30-50");
            priceList.add("50-100");
            priceList.add("100-200");
            priceList.add("200");

            ArrayList<String> dataList = new ArrayList<>();
            dataList.add(getActivity().getResources().getString(R.string.unlimited));
            dataList.add("0-30" + getString(R.string.wan));
            dataList.add("30-50" + getString(R.string.wan));
            dataList.add("50-100" + getString(R.string.wan));
            dataList.add("100-200" + getString(R.string.wan));
            dataList.add("200" + getString(R.string.wan) + getString(R.string.up));

            mPricePopListAdapter.setData(dataList);
        }

        if (mPricePop.isShowing()){
            mPricePop.dismiss();
            return;
        }
        if (condition.getTotalPrice() != null){
            mPricePopListAdapter.setKeyword(condition.getTotalPrice()
                    + getActivity().getResources().getString(R.string.wan));
            if (condition.getTotalPrice().equals("200")){
                mPricePopListAdapter.setKeyword(condition.getTotalPrice()
                        + getActivity().getResources().getString(R.string.wan)
                        + getString(R.string.up));
            }
            else if(condition.getTotalPrice().equals("0")){
                mPricePopListAdapter.setKeyword(getActivity().getResources().getString(R.string.unlimited));
            }
        }
        else {
            mPricePopListAdapter.setKeyword(null);
        }
        mMaskView.setVisibility(View.VISIBLE);
        mPricePop.showAsDropDown(mPopLineView, Gravity.BOTTOM, 0, 0);

    }

    private void typePop(){

        if (mTypePop == null){
            View popView = getActivity().getLayoutInflater().inflate(R.layout.pop_type, null);
            int width = DeviceInfoTool.getScreenWidth(getActivity());
            int height = DeviceInfoTool.getScreenHeight(getActivity());

            mTypePop = new PopupWindow(popView, width, height, true);

            mTypePop.setBackgroundDrawable(new ColorDrawable());
            mTypePop.setOutsideTouchable(true);

            mTypeBgView = popView.findViewById(R.id.root_view);
            mTypePopListView = popView.findViewById(R.id.type_list);

            mTypePopListView.setVerticalScrollBarEnabled(false);

            mTypeBgView.setOnClickListener(this);
            mTypePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mMaskView.setVisibility(View.GONE);
                }
            });

            mTypePopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    if (position > 0){
                        condition.setRooms(typeList.get(position));
                    }
                    else {
                        condition.setRooms(null);
                    }

                    search();
                    mTypePop.dismiss();
                    updateUI();
                }
            });

            mTypePopListAdapter = new PopListAdapter(getActivity());
            mTypePopListView.setAdapter(mTypePopListAdapter);

            typeList = new ArrayList<>();
            typeList.add("-1");
            typeList.add("1");
            typeList.add("2");
            typeList.add("3");
            typeList.add("4");
            typeList.add("5");
            typeList.add("6");

            ArrayList<String> dataList = new ArrayList<>();
            dataList.add(getActivity().getResources().getString(R.string.unlimited));
            dataList.add("1" + getString(R.string.room_unit));
            dataList.add("2" + getString(R.string.room_unit));
            dataList.add("3" + getString(R.string.room_unit));
            dataList.add("4" + getString(R.string.room_unit));
            dataList.add("5" + getString(R.string.room_unit));
            dataList.add("5" + getString(R.string.room_unit) + getString(R.string.up));

            mTypePopListAdapter.setData(dataList);
        }

        if (mTypePop.isShowing()){
            mTypePop.dismiss();
            return;
        }
        if (condition.getRooms() != null){
            mTypePopListAdapter.setKeyword(condition.getRooms()
                    + getActivity().getResources().getString(R.string.room_unit));

            if (condition.getRooms().equals("6")){
                mPricePopListAdapter.setKeyword("5"
                        + getString(R.string.room_unit)
                        + getString(R.string.up));
            }
            else if(condition.getRooms().equals("-1")){
                mPricePopListAdapter.setKeyword(getActivity().getResources().getString(R.string.unlimited));
            }
        }
        else {
            mTypePopListAdapter.setKeyword(null);
        }
        mMaskView.setVisibility(View.VISIBLE);
        mTypePop.showAsDropDown(mPopLineView, Gravity.BOTTOM, 0, 0);

    }

    private void morePop(){
        if (mMorePop == null){
            View popView = getActivity().getLayoutInflater().inflate(R.layout.pop_more, null);
            int width = DeviceInfoTool.getScreenWidth(getActivity());
            int height = DeviceInfoTool.getScreenHeight(getActivity());

            mMorePop = new PopupWindow(popView, width, height, true);

            mMorePop.setBackgroundDrawable(new ColorDrawable());
            mMorePop.setOutsideTouchable(true);

            mMoreBgView = popView.findViewById(R.id.root_view);
            mAreaFlow = popView.findViewById(R.id.flow_area);
            mOrientationFlow = popView.findViewById(R.id.flow_orientation);
            mSortFlow = popView.findViewById(R.id.flow_sort);
            mDecorateFlow = popView.findViewById(R.id.flow_decorate);
            mMoreClearBtn = popView.findViewById(R.id.btn_clear);
            mMoreConfirmBtn = popView.findViewById(R.id.btn_confirm);

            mMoreBgView.setOnClickListener(this);
            mMoreClearBtn.setOnClickListener(this);
            mMoreConfirmBtn.setOnClickListener(this);

            mMorePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mMaskView.setVisibility(View.GONE);
                }
            });

            ArrayList<String> areaList = new ArrayList<>();
            areaList.add("50" + getString(R.string.area_unit) + getString(R.string.down));
            areaList.add("50 - 70" + getString(R.string.area_unit));
            areaList.add("70 - 90" + getString(R.string.area_unit));
            areaList.add("90 - 120" + getString(R.string.area_unit));
            areaList.add("120" + getString(R.string.area_unit) + getString(R.string.up));
            mAreaFlow.setFlowLayout(areaList, new FlowLayout.OnItemClickListener() {
                @Override
                public void onItemClick(String content) {
                    mAreaFlow.setSelectedItem(content);
                    if (mAreaFlow.getSelectedItem() != null){
                        if (mAreaFlow.getSelectedItem().equals("50" + getString(R.string.area_unit) + getString(R.string.down))){
                            condition.setArea("0-50");
                        }
                        else if(mAreaFlow.getSelectedItem().equals("50 - 70" + getString(R.string.area_unit))){
                            condition.setArea("50-70");
                        }
                        else if(mAreaFlow.getSelectedItem().equals("70 - 90" + getString(R.string.area_unit))){
                            condition.setArea("70-90");
                        }
                        else if(mAreaFlow.getSelectedItem().equals("90 - 120" + getString(R.string.area_unit))){
                            condition.setArea("90-120");
                        }
                        else if(mAreaFlow.getSelectedItem().equals("120" + getString(R.string.area_unit) + getString(R.string.up))){
                            condition.setArea("120");
                        }
                    }
                    else {
                        condition.setArea(null);
                    }
                    updateUI();
                }
            });

            ArrayList<String> orientationList = new ArrayList<>();
            orientationList.add(getString(R.string.orientation_1));
            orientationList.add(getString(R.string.orientation_2));
            orientationList.add(getString(R.string.orientation_3));
            orientationList.add(getString(R.string.orientation_4));
            orientationList.add(getString(R.string.orientation_5));
            orientationList.add(getString(R.string.orientation_6));
            orientationList.add(getString(R.string.orientation_7));
            orientationList.add(getString(R.string.orientation_8));
            mOrientationFlow.setFlowLayout(orientationList, new FlowLayout.OnItemClickListener() {
                @Override
                public void onItemClick(String content) {
                    mOrientationFlow.setSelectedItem(content);
                    if (mOrientationFlow.getSelectedItem() != null){
                        if (mOrientationFlow.getSelectedItem().equals(getString(R.string.orientation_1))){
                            condition.setOrientation(1);
                        }
                        else if (mOrientationFlow.getSelectedItem().equals(getString(R.string.orientation_2))){
                            condition.setOrientation(2);
                        }
                        else if (mOrientationFlow.getSelectedItem().equals(getString(R.string.orientation_3))){
                            condition.setOrientation(3);
                        }
                        else if (mOrientationFlow.getSelectedItem().equals(getString(R.string.orientation_4))){
                            condition.setOrientation(4);
                        }
                        else if (mOrientationFlow.getSelectedItem().equals(getString(R.string.orientation_5))){
                            condition.setOrientation(5);
                        }
                        else if (mOrientationFlow.getSelectedItem().equals(getString(R.string.orientation_6))){
                            condition.setOrientation(6);
                        }
                        else if (mOrientationFlow.getSelectedItem().equals(getString(R.string.orientation_7))){
                            condition.setOrientation(7);
                        }
                        else if (mOrientationFlow.getSelectedItem().equals(getString(R.string.orientation_8))){
                            condition.setOrientation(8);
                        }
                    }
                    else {
                        condition.setOrientation(0);
                    }
                    updateUI();
                }
            });

            ArrayList<String> decorateList = new ArrayList<>();
            decorateList.add(getString(R.string.decorate_1));
            decorateList.add(getString(R.string.decorate_2));
            decorateList.add(getString(R.string.decorate_3));
            decorateList.add(getString(R.string.decorate_4));
            decorateList.add(getString(R.string.decorate_5));
            mDecorateFlow.setFlowLayout(decorateList, new FlowLayout.OnItemClickListener() {
                @Override
                public void onItemClick(String content) {
                    mDecorateFlow.setSelectedItem(content);
                    if (mDecorateFlow.getSelectedItem() != null){
                        if (mDecorateFlow.getSelectedItem().equals(getString(R.string.decorate_1))){
                            condition.setDecorate(1);
                        }
                        else if (mDecorateFlow.getSelectedItem().equals(getString(R.string.decorate_2))){
                            condition.setDecorate(2);
                        }
                        else if (mDecorateFlow.getSelectedItem().equals(getString(R.string.decorate_3))){
                            condition.setDecorate(3);
                        }
                        else if (mDecorateFlow.getSelectedItem().equals(getString(R.string.decorate_4))){
                            condition.setDecorate(4);
                        }
                        else if (mDecorateFlow.getSelectedItem().equals(getString(R.string.decorate_5))){
                            condition.setDecorate(5);
                        }
                    }
                    else {
                        condition.setDecorate(0);
                    }
                    updateUI();
                }
            });


            ArrayList<String> sortList = new ArrayList<>();
            sortList.add(getString(R.string.sort_price));
            sortList.add(getString(R.string.sort_area));
            sortList.add(getString(R.string.sort_publish));
            sortList.add(getString(R.string.sort_open));
            mSortFlow.setFlowLayout(sortList, new FlowLayout.OnItemClickListener() {
                @Override
                public void onItemClick(String content) {
                    mSortFlow.setSelectedItem(content);
                    if (mSortFlow.getSelectedItem() != null){
                        if (mSortFlow.getSelectedItem().equals(getString(R.string.sort_price))){
                            condition.setSort(1);
                        }
                        else if (mSortFlow.getSelectedItem().equals(getString(R.string.sort_publish))){
                            condition.setSort(2);
                        }
                        else if (mSortFlow.getSelectedItem().equals(getString(R.string.sort_open))){
                            condition.setSort(3);
                        }
                        else if (mSortFlow.getSelectedItem().equals(getString(R.string.sort_area))){
                            condition.setSort(4);
                        }
                    }
                    else {
                        condition.setSort(0);
                    }
                    updateUI();
                }
            });

        }

        if (mMorePop.isShowing()){
            mMorePop.dismiss();
            return;
        }

        mMaskView.setVisibility(View.VISIBLE);
        mMorePop.showAsDropDown(mPopLineView, Gravity.BOTTOM, 0, 0);
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
                condition.setIsResale(0);
                break;

            case 2:
                mRightView.setSelected(true);
                mRightText.setSelected(true);
                condition.setIsResale(1);
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
