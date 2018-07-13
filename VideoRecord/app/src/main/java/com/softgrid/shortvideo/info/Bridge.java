package com.softgrid.shortvideo.info;

import android.content.Context;
import com.softgrid.shortvideo.TabActivity;
import com.softgrid.shortvideo.model.Building;
import com.softgrid.shortvideo.model.User;


public class Bridge {

	private static Bridge info;

	private TabActivity mainActivity;

	public Building building;
	private User user;
	
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

	}


}