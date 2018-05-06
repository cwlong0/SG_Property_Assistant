package com.softgrid.shortvideo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.softgrid.shortvideo.utils.CameraFunc;

import org.w3c.dom.Text;

import java.io.IOException;

/**
 * Created by Raymond Pen on 2018/4/10.
 */

public class CameraActivity extends Activity {
    Context mContext;
    SurfaceView mSurfaceview;
    SurfaceHolder mSurfaceHolder;
    Handler mHandler = new Handler();
    CameraFunc cameraFunc;
    View recordBtn;
    TextView timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, flag);
        setContentView(R.layout.activity_camera);
        mContext = this;
        mSurfaceview = (SurfaceView) findViewById(R.id.surfaceViewCamera);
        mSurfaceHolder = mSurfaceview.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }

            @Override
            public void surfaceCreated(final SurfaceHolder holder) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!cameraFunc.cameraStart(holder)) {
                            //TODO: exit
                        }
                    }
                });
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraFunc.cameraStop();
            }

        });
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //mPuServer = PhotoUploadServer.getInstance(mContext);
        cameraFunc = new CameraFunc(getApplication(), mSurfaceview);
        recordBtn = findViewById(R.id.record_btn);
        timeText = (TextView) findViewById(R.id.video_time_text);

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraFunc.isRecording()) {
                    cameraFunc.stopRecording();
                    recordBtn.setBackgroundResource(R.drawable.icon_video_on);
                } else {
                    cameraFunc.startRecord();
                    recordBtn.setBackgroundResource(R.drawable.icon_video_off);
                }
            }
        });

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("VoiceCmdFunc", "CameraActivity stopListen");
        if (cameraFunc.isRecording()) {
            cameraFunc.interruptRecording();
        }
        Log.e("ScheduleCenter", "CameraActivity onPause End");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraFunc.cameraStop();
    }

}
