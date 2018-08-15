package com.softgrid.shortvideo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.TabActivity;
import com.softgrid.shortvideo.info.Bridge;
import com.softgrid.shortvideo.model.User;

/**
 * Created by tianfeng on 2017/12/10.
 */

public class TabMeFragment extends BaseFragment {

    private View mUserLayout;
    private ImageView mHeadIcon;
    private TextView mTitleText;
    private TextView mDescText;

    private View mFavBtn;
    private View mTransactionBtn;
    private View mLoanBtn;
    private View mBespokeBtn;

    private View mQaView;
    private View mLoanReckonView;
    private View mLawyerView;
    private View mBankerView;
    private View mIntermediaryView;

    private View mLoginView;
    private TextView mLoginText;

    private User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.tab_me, container, false);
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
        if (v == mUserLayout){

        }
        else if(v == mFavBtn){
            Bridge.getInstance().gotoFragment(TabActivity.FRAG_CollectionList);
        }
        else if(v == mTransactionBtn){
            Bridge.getInstance().gotoFragment(TabActivity.FRAG_TransactionList);
        }
        else if(v == mLoanBtn){
            Bridge.getInstance().gotoFragment(TabActivity.FRAG_LoanList);
        }
        else if(v == mBespokeBtn){
            Bridge.getInstance().gotoFragment(TabActivity.FRAG_BespokeList);
        }
        else if(v == mQaView){

        }
        else if(v == mLoanReckonView){

        }
        else if(v == mLawyerView){
            Bridge.getInstance().gotoFragment(TabActivity.FRAG_LawyerList);
        }
        else if(v == mBankerView){
            Bridge.getInstance().gotoFragment(TabActivity.FRAG_BankerList);
        }
        else if(v == mIntermediaryView){
            Bridge.getInstance().gotoFragment(TabActivity.FRAG_IntermediaryList);
        }
        else if(v == mLoginView){

        }
    }

    public void initUI(View view) {
        initHeadView(view, this);

        mUserLayout = view.findViewById(R.id.user_layout);
        mHeadIcon = view.findViewById(R.id.user_icon);
        mTitleText = view.findViewById(R.id.user_name);
        mDescText = view.findViewById(R.id.user_desc);

        mFavBtn = view.findViewById(R.id.user_fav_layout);
        mTransactionBtn = view.findViewById(R.id.user_transaction_layout);
        mLoanBtn = view.findViewById(R.id.user_loan_layout);
        mBespokeBtn = view.findViewById(R.id.user_bespoke_layout);

        mQaView = view.findViewById(R.id.user_qa_layout);
        mLoanReckonView = view.findViewById(R.id.user_loan_reckon_layout);
        mLawyerView = view.findViewById(R.id.user_lawyer_layout);
        mBankerView = view.findViewById(R.id.user_banker_layout);
        mIntermediaryView = view.findViewById(R.id.user_intermediary_layout);

        mLoginView = view.findViewById(R.id.user_login_layout);
        mLoginText = view.findViewById(R.id.user_login_text);
    }

    private void addActions(){
        mUserLayout.setOnClickListener(this);
        mFavBtn.setOnClickListener(this);
        mTransactionBtn.setOnClickListener(this);
        mLoanBtn.setOnClickListener(this);
        mBespokeBtn.setOnClickListener(this);
        mQaView.setOnClickListener(this);
        mLoanReckonView.setOnClickListener(this);
        mLawyerView.setOnClickListener(this);
        mBankerView.setOnClickListener(this);
        mIntermediaryView.setOnClickListener(this);
        mLoginView.setOnClickListener(this);
    }

    public void initData(){
        titleText.setText(R.string.tab_4);
    }

    private void updateUI(){

    }
}
