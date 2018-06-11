package com.softgrid.shortvideo;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.softgrid.shortvideo.upload.VideoListPersistence;

import lm.kit.annotation.BindLayout;
import lm.kit.annotation.FindView;
import lm.kit.lazy.LazyKit;

@BindLayout(layout = R.layout.activity_main)
public class MainActivity extends Activity {

    @FindView(id = R.id.video_thumb)
    ImageView video_thumb;

    @FindView(id = R.id.videoView)
    VideoView video_view;

    @FindView(id = R.id.video_container)
    View video_container;

    VideoListPersistence vlp;
    String videoPath = "";
    int videoPosition = -100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, flag);
        vlp = new VideoListPersistence(this);

//        video_container = findViewById(R.id.video_container);
//        video_thumb = findViewById(R.id.video_thumb);
//        video_view = findViewById(R.id.videoView);
        video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                video_view.setVisibility(View.INVISIBLE);
            }
        });
        video_view.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("raymond", "video error " + what);
                return true;
            }
        });

        findViewById(R.id.record_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        video_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(videoPath)) return;
                video_view.setVisibility(View.VISIBLE);
                if (video_view.isPlaying()) {
                    videoPosition = video_view.getCurrentPosition();
                    video_view.stopPlayback();
                } else {
                    video_view.setVideoURI(Uri.parse(videoPath));
                    video_view.start();
                    if (videoPosition != -100)
                        video_view.seekTo(videoPosition);
                    videoPosition = -100;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (video_view != null && video_view.isPlaying()) {
            video_view.stopPlayback();
        }
        video_view.setVisibility(View.INVISIBLE);
        videoPosition = -100;
        videoPath = vlp.getVideo();
        if (!TextUtils.isEmpty(videoPath)) {
            ImageLoader.getInstance().displayImage("file://" + videoPath.replace(".mp4", ".jpg"), video_thumb);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (video_view.isPlaying()) {
            video_view.stopPlayback();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
