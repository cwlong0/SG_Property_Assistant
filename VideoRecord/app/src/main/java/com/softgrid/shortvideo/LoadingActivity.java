package com.softgrid.shortvideo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by tianfeng on 2018/7/4.
 */

public class LoadingActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        start();

    }

    private void start() {

        Intent intent = new Intent(this, TabActivity.class);
        startActivity(intent);
        finish();

    }
}
