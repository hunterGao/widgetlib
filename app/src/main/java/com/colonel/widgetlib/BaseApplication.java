package com.colonel.widgetlib;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by colonel on 2017/11/21.
 */

public class BaseApplication extends Application {

    public static Context context;
    public static RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        refWatcher = LeakCanary.install(this);
    }
}
