package com.softgrid.shortvideo.tool;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.softgrid.shortvideo.R;
import com.softgrid.shortvideo.utils.AppHelper;

import lm.widget.PickerTextView;

/**
 * Created by tianfeng on 2018/7/24.
 */

public class DatePopView implements View.OnClickListener{

    private Activity activity;

    private DatePopListener listener;

    private PopupWindow mDatePop;
    private View dateOutsideView;             //空白区域
    private View dateMenuView;                //菜单区域
    private TextView dateCancelText;          //取消
    private TextView dateConfirmText;         //确定
    private PickerTextView datePicker01;      //日期选择view  第一级
    private PickerTextView datePicker02;      //日期选择view  第二级
    private PickerTextView datePicker03;      //日期选择view  第三级

    private int startYear = 2010;
    private int countYear = 30;

    private DatePopView(){

    }

    public DatePopView(Activity activity){
        this.activity = activity;
        init();
    }

    public DatePopView(Activity activity, DatePopListener listener){
        this.activity = activity;
        this.listener = listener;
        init();
    }

    public DatePopView(Activity activity, int countYear, DatePopListener listener){
        this.countYear = countYear;
        this.activity = activity;
        this.listener = listener;
        init();
    }

    public DatePopView(Activity activity, int countYear, int startYear, DatePopListener listener){
        this.countYear = countYear;
        this.startYear = startYear;
        this.activity = activity;
        this.listener = listener;
        init();
    }

    public void setListener(DatePopListener listener) {
        this.listener = listener;
    }

    private void init(){

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.pop_date, null);

        dateOutsideView = view.findViewById(R.id.top_view);
        dateMenuView = view.findViewById(R.id.bottom_layout);
        dateCancelText = view.findViewById(R.id.cancel);
        dateConfirmText = view.findViewById(R.id.confirm);
        datePicker01 = view.findViewById(R.id.location_picker_01);
        datePicker02 = view.findViewById(R.id.location_picker_02);
        datePicker03 = view.findViewById(R.id.location_picker_03);

        dateOutsideView.setOnClickListener(this);
        dateConfirmText.setOnClickListener(this);
        dateCancelText.setOnClickListener(this);

        datePicker01.setAdapter(new PickerTextView.Adapter() {
            @Override
            public int getCount() {
                return countYear;
            }

            @Override
            public String getString(int position) {
                return ((startYear + position) + "");
            }
        });

        datePicker02.setAdapter(new PickerTextView.Adapter() {
            @Override
            public int getCount() {
                return 12;
            }

            @Override
            public String getString(int position) {
                return (1 + position) + "";
            }
        });

        datePicker03.setAdapter(new PickerTextView.Adapter() {
            @Override
            public int getCount() {
                return 31;
            }

            @Override
            public String getString(int position) {
                return (1 + position) + "";
            }
        });

        datePicker02.setOnPositionChangeListener(new PickerTextView.OnPositionChangeListener() {
            @Override
            public void onPositionChange(PickerTextView sender, final int position) {

                PickerTextView.Adapter adapter = new PickerTextView.Adapter() {
                    @Override
                    public int getCount() {
                        if (position == 1){
                            if ((datePicker01.getPosition() + startYear)%4 == 0){
                                return 29;
                            }
                            else {
                                return 28;
                            }

                        }
                        else if(position == 0 || position == 2 || position == 4 ||
                                position == 6 || position == 7 || position == 9 ||
                                position == 11){
                            return 31;
                        }
                        return 30;
                    }

                    @Override
                    public String getString(int pos) {
                        return (1 + pos) + "";
                    }
                };

                int oldPos = datePicker03.getPosition();

                datePicker03.setAdapter(adapter);

                int lastPos = adapter.getCount() - 1;
                if (oldPos > lastPos){
                    datePicker03.setPosition(lastPos);
                }
            }
        });

        mDatePop = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mDatePop.setFocusable(false);
    }


    @Override
    public void onClick(View view) {
        if (view == dateConfirmText){
            if (listener != null){

                String date = (datePicker01.getPosition() + startYear) + "-" +
                        (datePicker02.getPosition() + 1) + "-" +
                        (datePicker03.getPosition() + 1);

                listener.onConfirm(AppHelper.getInstance().dateStringToStamp(date, "yyyy-MM-dd"));
            }
            dismiss();
        }
        else if(view == dateCancelText){
            if (listener != null){
                listener.onCancel();
            }
            dismiss();
        }
        else if(view == dateOutsideView){
            dismiss();
        }
    }

    public void show(View view){
        mDatePop.showAsDropDown(view, Gravity.BOTTOM, 0, 0);
        AppHelper.getInstance().startAnimation(activity, dateMenuView, mDatePop, true);
    }

    public void dismiss(){
        mDatePop.dismiss();
    }

    public interface DatePopListener{

        abstract void onCancel();

        abstract void onConfirm(long time);
    }
}
