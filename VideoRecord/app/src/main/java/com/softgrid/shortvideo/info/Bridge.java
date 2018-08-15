package com.softgrid.shortvideo.info;

import android.content.Context;
import com.softgrid.shortvideo.TabActivity;
import com.softgrid.shortvideo.model.Bespoke;
import com.softgrid.shortvideo.model.Building;
import com.softgrid.shortvideo.model.Loans;
import com.softgrid.shortvideo.model.Transaction;
import com.softgrid.shortvideo.model.User;

public class Bridge {

	private static Bridge info;

	private TabActivity mainActivity;

	public Building building;
	public User user;
	public Loans loans;
	public Transaction transaction;
	public Bespoke bespoke;
	
	private Bridge(){

	}

	public static Bridge getInstance() {
		if (info == null) {
			info = new Bridge();
		}
		return info;
	}

	public void setMainActivity(TabActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	public TabActivity getMainActivity() {
		return mainActivity;
	}

	public void gotoFragment(int type){
		this.mainActivity.addFragment(type);
	}

	public void goBack(){
		this.mainActivity.removeTopFragment(true);
	}

	public void goRoot(){
		this.mainActivity.removeAllFragment();
	}


	/**
	 * 显示房产详情
	 * */
	public void showBuilding(Context context, Building building){
		this.building = building;
		gotoFragment(TabActivity.FRAG_BUILDING);
	}

	/**
	 * 显示用户详情
	 * */
	public void showUser(Context context, User user){
		this.user = user;
		gotoFragment(TabActivity.FRAG_USER);

	}

	/**
	 * 显示贷款详情
	 * */
	public void showLoan(Context context, Loans loans){
		this.loans = loans;
		gotoFragment(TabActivity.FRAG_LoanDetail);
	}

	/**
	 * 显示交易详情
	 * */
	public void showTransaction(Context context, Transaction transaction){
		this.transaction = transaction;
		gotoFragment(TabActivity.FRAG_TransactionDetail);
	}

	/**
	 * 显示预约详情
	 * */
	public void showBespoke(Context context, Bespoke bespoke){
		this.bespoke = bespoke;
		gotoFragment(TabActivity.FRAG_BespokeDetail);
	}


}