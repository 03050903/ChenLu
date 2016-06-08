package com.chenlu.base;

import android.app.Application;

import com.github.promeg.xlog_android.lib.XLogConfig;

/**
 * Created by C&C on 2016/6/8.
 */

public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        XLogConfig.config(XLogConfig.newConfigBuilder(this).build());

    }
}
