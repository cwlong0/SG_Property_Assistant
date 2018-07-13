package com.softgrid.shortvideo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softgrid.shortvideo.fragment.FragmentBuildingDetail;
import com.softgrid.shortvideo.fragment.FragmentIntermediaryList;
import com.softgrid.shortvideo.fragment.TabHomeFragment;
import com.softgrid.shortvideo.fragment.TabMeFragment;
import com.softgrid.shortvideo.fragment.TabMsgFragment;
import com.softgrid.shortvideo.fragment.TabSearchFragment;
import com.softgrid.shortvideo.info.Bridge;

import java.util.ArrayList;

/**
 * Created by tianfeng on 2018/7/4.
 */

public class TabActivity extends FragmentActivity implements View.OnClickListener {

    public static final int TAB_HOME = 1;
    public static final int TAB_SEARCH = 2;
    public static final int TAB_MSG = 3;
    public static final int TAB_ME = 4;

    public static final int FRAG_BUILDING = 100;
    public static final int FRAG_IntermediaryList = 200;

    private RelativeLayout mHomeLayout;
    private RelativeLayout mSearchLayout;
    private RelativeLayout mMsgLayout;
    private RelativeLayout mMeLayout;

    private ImageView mHomeImageView;
    private ImageView mSearchImageView;
    private ImageView mMsgImageView;
    private ImageView mMeImageView;

    private TextView mHomeText;
    private TextView mSearchText;
    private TextView mMsgText;
    private TextView mMeText;

    private Fragment mHomeFragment;
    private Fragment mSearchFragment;
    private Fragment mMsgFragment;
    private Fragment mMeFragment;

    private int currentTab = 1;
    private int preTab = 0;

    private ArrayList<Fragment> taskList;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if(savedInstanceState != null &&
                savedInstanceState.getBoolean("isMainActivityDestroy", false)){

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            Fragment fragment;
            fragment = getSupportFragmentManager().findFragmentByTag("mHomeFragment");
            if (fragment != null){
                transaction.remove(fragment);
            }
            fragment = getSupportFragmentManager().findFragmentByTag("mSearchFragment");
            if (fragment != null){
                transaction.remove(fragment);
            }
            fragment = getSupportFragmentManager().findFragmentByTag("mMsgFragment");
            if (fragment != null){
                transaction.remove(fragment);
            }
            fragment = getSupportFragmentManager().findFragmentByTag("mMeFragment");
            if (fragment != null){
                transaction.remove(fragment);
            }
            transaction.commit();
            savedInstanceState.putBoolean("isMainActivityDestroy", false);
        }

        setContentView(R.layout.layout_tab);

        Bridge.getInstance().setMainActivity(this);

        taskList = new ArrayList<>();

        initUI();
        addActions();
        initFragment();
        switchTab(currentTab);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (taskList.size() == 0){
            switchTab(currentTab);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mHomeFragment = null;
        mSearchFragment = null;
        mMsgFragment = null;
        mMeFragment = null;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        outState.putBoolean("isMainActivityDestroy", true);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == mHomeLayout) {
            switchTab(TAB_HOME);
        }
        else if (v == mSearchLayout) {
            switchTab(TAB_SEARCH);
        }

        else if (v == mMsgLayout) {
            switchTab(TAB_MSG);
        }

        else if (v == mMeLayout) {
            switchTab(TAB_ME);
        }
    }

    @Override
    public void onBackPressed() {

        if(taskList.size() > 0){
            removeTopFragment(true);
        }
        else{
            finish();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        return super.dispatchKeyEvent(event);
    }

    private void initUI() {

        mHomeLayout = (RelativeLayout) findViewById(R.id.tab_1);
        mHomeImageView = (ImageView) findViewById(R.id.tab_1_image);
        mHomeText = (TextView) findViewById(R.id.tab_1_text);

        mSearchLayout = (RelativeLayout) findViewById(R.id.tab_2);
        mSearchImageView = (ImageView) findViewById(R.id.tab_2_image);
        mSearchText = (TextView) findViewById(R.id.tab_2_text);

        mMsgLayout = (RelativeLayout) findViewById(R.id.tab_3);
        mMsgImageView = (ImageView) findViewById(R.id.tab_3_image);
        mMsgText = (TextView) findViewById(R.id.tab_3_text);

        mMeLayout = (RelativeLayout) findViewById(R.id.tab_4);
        mMeImageView = (ImageView) findViewById(R.id.tab_4_image);
        mMeText = (TextView) findViewById(R.id.tab_4_text);



    }


    private void addActions() {
        mHomeLayout.setOnClickListener(this);
        mSearchLayout.setOnClickListener(this);
        mMsgLayout.setOnClickListener(this);
        mMeLayout.setOnClickListener(this);
    }

    private void initFragment() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        mHomeFragment = new TabHomeFragment();
        mSearchFragment = new TabSearchFragment();
        mMsgFragment = new TabMsgFragment();
        mMeFragment = new TabMeFragment();

        transaction.add(R.id.content, mHomeFragment, "mHomeFragment");
        transaction.add(R.id.content, mSearchFragment, "mSearchFragment");
        transaction.add(R.id.content, mMsgFragment, "mMsgFragment");
        transaction.add(R.id.content, mMeFragment, "mMeFragment");

        transaction.commit();

        transaction.hide(mHomeFragment);
        transaction.hide(mSearchFragment);
        transaction.hide(mMsgFragment);
        transaction.hide(mMeFragment);

    }

    public void addFragment(int type){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.anim_right_in,
                R.anim.anim_left_out,
                R.anim.anim_left_in,
                R.anim.anim_right_out
        );
        if (type == FRAG_BUILDING){
            Fragment fragment = new FragmentBuildingDetail();
            taskList.add(0, fragment);
            transaction.add(R.id.content, fragment);
        }
        else if (type == FRAG_IntermediaryList){
            Fragment fragment = new FragmentIntermediaryList();
            taskList.add(0, fragment);
            transaction.add(R.id.content, fragment);
        }
        transaction.commit();
    }

    public void removeTopFragment(boolean anmate){
        if (taskList.size() == 0){
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (taskList.size() == 1){

        }
        if (anmate){
            transaction.setCustomAnimations(
                    R.anim.anim_left_in,
                    R.anim.anim_right_out,
                    R.anim.anim_right_in,
                    R.anim.anim_left_out
            );
        }
        transaction.remove(taskList.get(0));
        transaction.commit();
        taskList.remove(0);
    }

    public void removeAllFragment(){
        if (taskList.size() == 0){
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        for (int i = 0; i < taskList.size(); i++){
            transaction.remove(taskList.get(i));
        }
        transaction.commit();
        taskList.clear();
    }

    public void switchTab(int tab) {
        if (tab != -1) {
            preTab = currentTab;
            currentTab = tab;
        }

        if (currentTab == TAB_HOME || currentTab == TAB_SEARCH || currentTab == TAB_MSG ||
                currentTab == TAB_ME) {
            mHomeImageView.setSelected(false);
            mSearchImageView.setSelected(false);
            mMsgImageView.setSelected(false);
            mMeImageView.setSelected(false);

            mHomeText.setSelected(false);
            mSearchText.setSelected(false);
            mMsgText.setSelected(false);
            mMeText.setSelected(false);
        }

        switch (currentTab) {
            case TAB_HOME:
                mHomeImageView.setSelected(true);
                mHomeText.setSelected(true);
                break;

            case TAB_SEARCH:
                mSearchImageView.setSelected(true);
                mSearchText.setSelected(true);
                break;

            case TAB_MSG:
                mMsgImageView.setSelected(true);
                mMsgText.setSelected(true);
                break;

            case TAB_ME:
                mMeImageView.setSelected(true);
                mMeText.setSelected(true);
                break;

            default:
                break;
        }

        gotoFragmentByTab(currentTab);

        if (mHandler == null){
            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    removeAllFragment();
                }
            };
        }
        mHandler.sendEmptyMessageDelayed(0, 200);
    }

    private void gotoFragmentByTab(int tab) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (currentTab < preTab) {
            transaction.setCustomAnimations(
                    R.anim.anim_left_in,
                    R.anim.anim_right_out,
                    R.anim.anim_right_in,
                    R.anim.anim_left_out
            );
        }
        else{
            transaction.setCustomAnimations(
                    R.anim.anim_right_in,
                    R.anim.anim_left_out,
                    R.anim.anim_left_in,
                    R.anim.anim_right_out
            );
        }

        if (!mHomeFragment.isHidden() && tab != TAB_HOME) {
            transaction.hide(mHomeFragment);
        }
        if (!mSearchFragment.isHidden() && tab != TAB_SEARCH) {
            transaction.hide(mSearchFragment);
        }
        if (!mMsgFragment.isHidden() && tab != TAB_MSG) {
            transaction.hide(mMsgFragment);
        }
        if (!mMeFragment.isHidden() && tab != TAB_ME) {
            transaction.hide(mMeFragment);
        }

        switch (tab) {
            case TAB_HOME:
                if (mHomeFragment.isHidden()) {
                    transaction.show(mHomeFragment);
                }
                break;
            case TAB_SEARCH:
                if (mSearchFragment.isHidden()) {
                    transaction.show(mSearchFragment);
                }
                break;
            case TAB_MSG:
                if (mMsgFragment.isHidden()) {
                    transaction.show(mMsgFragment);
                }
                break;
            case TAB_ME:
                if (mMeFragment.isHidden()) {
                    transaction.show(mMeFragment);
                }
                break;


        }
        transaction.commit();

    }

}
