package com.softgrid.shortvideo.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.adapter.MsgAdapter;
import com.softgrid.shortvideo.callback.CallBackListener;
import com.softgrid.shortvideo.callback.ErrorInfo;
import com.softgrid.shortvideo.factory.LogicFactory;
import com.softgrid.shortvideo.info.Bridge;
import com.softgrid.shortvideo.model.Msg;

import java.util.ArrayList;

/**
 * Created by tianfeng on 2017/12/10.
 */

public class TabMsgFragment extends BaseFragment {


    private PullToRefreshListView mDataListView;

    private View mPromptView;

    private MsgAdapter mAdapter;

    private ArrayList<Msg> dataList;

    private boolean init = false;

    private boolean isLoading;

    private int page = 1;

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



    @SuppressLint("HandlerLeak")
    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!init){
                init = true;
                new Handler(){

                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 0){
                            mDataListView.setRefreshing();
                        }
                    }
                }.sendEmptyMessageDelayed(0,200);
            }
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

        mDataListView = view.findViewById(R.id.data_list);

        mPromptView = view.findViewById(R.id.prompt_layout);

        mDataListView.setMode(PullToRefreshBase.Mode.BOTH);


    }

    private void addActions(){

        mDataListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(false);
            }
        });

        mDataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });
    }

    public void initData(){
        titleText.setText(R.string.tab_3);

        dataList = new ArrayList<>();

        mAdapter = new MsgAdapter(getActivity());
        mDataListView.setAdapter(mAdapter);

        mPromptView.setVisibility(View.GONE);

    }

    private void updateUI(){

    }

    private void loadData(boolean reload){

        mPromptView.setVisibility(View.GONE);

        if (isLoading){
            return;
        }
        isLoading = true;

        if (reload){
            page = 1;
            dataList.clear();
            mAdapter.setItemsData(dataList);
        }
        else {
            page++;
        }
        LogicFactory.getInstance().getNetLogic().getNotice(getActivity(), page,
                new CallBackListener<ArrayList<Msg>>() {

                    @Override
                    public void onCache(ArrayList<Msg> data) {
                        dataList.addAll(data);
                        mAdapter.setItemsData(dataList);
                    }

                    @Override
                    public void onComplete(ArrayList<Msg> data) {
                        dataList.addAll(data);
                        mAdapter.setItemsData(dataList);
                        isLoading = false;
                        mDataListView.onRefreshComplete();
                        if (dataList.size() == 0){
                            mPromptView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        isLoading = false;
                        mDataListView.onRefreshComplete();
                        if (dataList.size() == 0){
                            mPromptView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
