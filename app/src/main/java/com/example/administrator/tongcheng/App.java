package com.example.administrator.tongcheng;

import android.app.Application;

import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2017/10/24.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this);
    }
}
