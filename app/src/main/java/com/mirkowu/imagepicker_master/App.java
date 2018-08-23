package com.mirkowu.imagepicker_master;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            Log.d("App", "In LeakCanary Analyzer Process");
            return;
        }
        LeakCanary.install(this);
    }
}
