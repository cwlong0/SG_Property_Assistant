package com.softgrid.shortvideo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.customView.CImageView;
import com.softgrid.shortvideo.customView.MarqueeTextView;

public class BaseFragment extends Fragment implements OnClickListener {

	public View rootLayout;
	public View rootLine;
	public MarqueeTextView titleText;
	public Button leftBtn;
	public Button rightBtn;
	public ProgressBar titleLoadingBar;
	public CImageView leftImage;
	public CImageView rightImage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public void initHeadView(View view, BaseFragment fragment){
		
		rootLayout = view.findViewById(R.id.title_root_layout);
		rootLine = view.findViewById(R.id.root_line);
		titleText = (MarqueeTextView) view.findViewById(R.id.title);
		leftBtn = (Button) view.findViewById(R.id.left_btn);
		rightBtn = (Button) view.findViewById(R.id.right_btn);
		titleLoadingBar = (ProgressBar) view.findViewById(R.id.title_loading_bar);
		leftImage = (CImageView) view.findViewById(R.id.left_btn_img);
		rightImage = (CImageView) view.findViewById(R.id.right_btn_img);
		
		titleText.setOnClickListener(fragment);
		leftBtn.setOnClickListener(fragment);
		rightBtn.setOnClickListener(fragment);
		titleLoadingBar.setOnClickListener(fragment);
		leftImage.setOnClickListener(fragment);
		rightImage.setOnClickListener(fragment);

		rootLine.setVisibility(View.GONE);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (!hidden) {

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
