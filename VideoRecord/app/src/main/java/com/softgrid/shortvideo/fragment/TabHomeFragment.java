package com.softgrid.shortvideo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.adapter.HomeRecommendAdapter;
import com.softgrid.shortvideo.callback.CallBackListener;
import com.softgrid.shortvideo.callback.ErrorInfo;
import com.softgrid.shortvideo.factory.LogicFactory;
import com.softgrid.shortvideo.info.Bridge;
import com.softgrid.shortvideo.model.Banner;
import com.softgrid.shortvideo.model.Building;

import java.util.ArrayList;

/**
 * Created by tianfeng on 2017/12/10.
 */

public class TabHomeFragment extends BaseFragment {


    private PullToRefreshListView mDataListView;

    private View mPromptView;

    private HomeRecommendAdapter mAdapter;

    private ArrayList<Building> dataList;

    private boolean init;
    private boolean loadedBanner;
    private boolean loadedItems;
    private boolean loadingBanner;
    private boolean loadingItems;

    private int currentType = 1;
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.tab_home, container, false);
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

        mDataListView = view.findViewById(R.id.data_list);

        mPromptView = view.findViewById(R.id.prompt_layout);

        mDataListView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private void addActions(){
        mDataListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (init){
                    loadData(false, true);
                }
                else {
                    loadData(true, true);
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(false, false);
            }
        });

        mDataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Bridge.getInstance().showBuilding(getActivity(),
                        (Building) mAdapter.getItem(position - 1));
            }
        });
    }

    public void initData(){

        titleText.setText(R.string.tab_1);

        init = false;
        dataList = new ArrayList<>();

        mAdapter = new HomeRecommendAdapter(getActivity());
        mDataListView.setAdapter(mAdapter);

        mAdapter.setClickItemListener(new HomeRecommendAdapter.ClickItemListener() {
            @Override
            public void onClick(int position) {
                switch (position){

                    case 1:
                        currentType = 1;
                        mDataListView.setRefreshing();
                        break;

                    case 2:
                        currentType = 2;
                        mDataListView.setRefreshing();
                        break;

                    case 3:
                        Toast.makeText(getActivity(), "咨询", Toast.LENGTH_SHORT).show();
                        break;

                    case 4:
                        Toast.makeText(getActivity(), "问答", Toast.LENGTH_SHORT).show();
                        break;

                    case 10:
                        Toast.makeText(getActivity(), "搜索", Toast.LENGTH_SHORT).show();
                        break;

                    case 11:
                        Toast.makeText(getActivity(), "地图搜索", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        mDataListView.setRefreshing();

    }

    private void updateUI(){

    }

    private void loadData(boolean reloadRecommend, boolean reload){

        mPromptView.setVisibility(View.GONE);

        if (reloadRecommend){

            if (loadingBanner){
                return;
            }
            loadingBanner = true;

            loadedBanner = false;
            LogicFactory.getInstance().getNetLogic().getCarousel(getActivity(),
                    new CallBackListener<ArrayList<Banner>>() {
                        @Override
                        public void onCache(ArrayList<Banner> data) {
                            mAdapter.setBannerData(data);
                        }

                        @Override
                        public void onComplete(ArrayList<Banner> data) {
                            mAdapter.setBannerData(data);
                            loadedBanner = true;
                            loadingBanner = false;
                            init = true;

                            if (loadedBanner && loadedItems){
                                mDataListView.onRefreshComplete();
                            }
                        }

                        @Override
                        public void onError(ErrorInfo errorInfo) {
                            loadedBanner = true;
                            loadingBanner = false;
                            if (loadedBanner && loadedItems){
                                mDataListView.onRefreshComplete();
                            }
                        }
                    });
        }

        //获取推荐房产
        if (loadingItems){
            return;
        }
        loadingItems = true;
        loadedItems = false;

        if (reload){
            page = 1;
            dataList.clear();
            mAdapter.setItemsData(dataList);
            if (currentType == 1){
                mAdapter.setTitleData(getActivity().getResources().getString(R.string.rank_new));
            }
            else if(currentType == 2){
                mAdapter.setTitleData(getActivity().getResources().getString(R.string.rank_resale));
            }
        }
        else {
            page++;
        }
        LogicFactory.getInstance().getNetLogic().getRecommendBuilding(getActivity(), currentType, page,
                new CallBackListener<ArrayList<Building>>() {
                    @Override
                    public void onCache(ArrayList<Building> data) {
                        dataList.addAll(data);
                        mAdapter.setItemsData(dataList);
                    }

                    @Override
                    public void onComplete(ArrayList<Building> data) {
                        dataList.addAll(data);
                        mAdapter.setItemsData(dataList);
                        loadedItems = true;
                        loadingItems = false;
                        if (loadedBanner && loadedItems){
                            mDataListView.onRefreshComplete();
                        }
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        loadedItems = true;
                        loadingItems = false;
                        if (loadedBanner && loadedItems){
                            mDataListView.onRefreshComplete();
                        }
                    }
                });
    }
}
